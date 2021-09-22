package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.blocks.WashingMachineBlock;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.capabilities.TileFluidStorageCapability;
import net.mabbouxj.colorful_journey.containers.WashingMachineContainer;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.mabbouxj.colorful_journey.utils.RecipeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WashingMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        INPUT(0),
        OUTPUT1(1),
        OUTPUT2(2);

        int id;

        Slots(int number) {
            id = number;
        }

        public int getId() {
            return id;
        }
    }

    public TileEnergyStorageCapability energyStorage;
    public TileFluidStorageCapability fluidStorage;
    private final LazyOptional<TileEnergyStorageCapability> energy;
    private final LazyOptional<TileFluidStorageCapability> fluid;
    private final LazyOptional<ItemStackHandler> inventory;
    private int recipeProgress = 0;
    private WashingMachineRecipe currentRecipe;

    public final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return WashingMachineTile.this.recipeProgress;
                case 1:
                    return WashingMachineTile.this.currentRecipe != null ? WashingMachineTile.this.currentRecipe.getProcessingTime(): -1;
                default:
                    throw new IllegalArgumentException("Invalid index: " + index);
            }
        }

        @Override
        public void set(int index, int value) {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public WashingMachineTile() {
        super(ModTiles.WASHING_MACHINE.get());
        this.energyStorage = new TileEnergyStorageCapability(this, ModConfigs.COMMON_CONFIG.WASHING_MACHINE_ENERGY_BUFFER.get(), 1000, false, true);
        this.energy = LazyOptional.of(() -> energyStorage);
        this.fluidStorage = new TileFluidStorageCapability(this, ModConfigs.COMMON_CONFIG.WASHING_MACHINE_FLUID_BUFFER.get());
        this.fluid = LazyOptional.of(() -> fluidStorage);
        this.inventory = LazyOptional.of(() -> new WashingMachineTile.ItemHandler(this));
    }

    private void checkForRecipe() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        inventory.ifPresent(inventory -> fluid.ifPresent(tank -> energy.ifPresent(buffer -> {
            if (currentRecipe != null && currentRecipe.matches(inventory, tank, buffer)) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(WashingMachineBlock.LIT, true), 3);
                return;
            }
            currentRecipe = RecipeUtils.getRecipes(this.level, ModRecipeTypes.WASHING_MACHINE).stream().filter(recipe -> recipe.matches(inventory, tank, buffer)).findFirst().orElse(null);
            if (currentRecipe == null) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(WashingMachineBlock.LIT, false), 3);
                recipeProgress = 0;
            }
        })));
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
        assert level != null;
        return new WashingMachineContainer(this, this.data, i, playerInventory, this.inventory.orElse(new ItemStackHandler(Slots.values().length)));
    }

    @Override
    public void tick() {

        if (this.level == null || this.level.isClientSide || currentRecipe == null)
            return;

        inventory.ifPresent(inventory -> fluid.ifPresent(tank -> energy.ifPresent(buffer -> {

            ItemStack recipeResult = currentRecipe.getResultItem().copy();
            ItemStack recipeResultAlt = currentRecipe.getResultItemAlt().copy();

            if (recipeResult != ItemStack.EMPTY && inventory.insertItem(Slots.OUTPUT1.getId(), recipeResult, true).getCount() == recipeResult.getCount())
                return;
            if (recipeResultAlt != ItemStack.EMPTY && inventory.insertItem(Slots.OUTPUT2.getId(), recipeResultAlt, true).getCount() == recipeResultAlt.getCount())
                return;
            if (buffer.extractEnergy(currentRecipe.getEnergyPerTick(), false) < currentRecipe.getEnergyPerTick())
                return;

            recipeProgress++;

            if (recipeProgress == currentRecipe.getProcessingTime()) {
                inventory.insertItem(Slots.OUTPUT1.getId(), recipeResult, false);
                inventory.insertItem(Slots.OUTPUT2.getId(), recipeResultAlt, false);
                inventory.getStackInSlot(Slots.INPUT.getId()).shrink(currentRecipe.getInputStack().getCount());
                tank.drain(currentRecipe.getInputFluid(), IFluidHandler.FluidAction.EXECUTE);
                recipeProgress = 0;
                currentRecipe = null;
                this.setChanged();
            }

        })));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }

    @Override
    public void load(BlockState stateIn, CompoundNBT compound) {
        super.load(stateIn, compound);
        inventory.ifPresent(h -> h.deserializeNBT(compound.getCompound("inv")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
        fluid.ifPresent(h -> h.deserializeNBT(compound.getCompound("fluid")));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inventory.ifPresent(h ->  compound.put("inv", h.serializeNBT()));
        energy.ifPresent(h -> compound.put("energy", h.serializeNBT()));
        fluid.ifPresent(h -> compound.put("fluid", h.serializeNBT()));
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        if (cap == CapabilityEnergy.ENERGY)
            return energy.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluid.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState stateIn, CompoundNBT tag) {
        load(stateIn, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public void setRemoved() {
        energy.invalidate();
        inventory.invalidate();
        fluid.invalidate();
        super.setRemoved();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.washing_machine");
    }

    public static class ItemHandler extends ItemStackHandler {

        private final WashingMachineTile tile;

        public ItemHandler(WashingMachineTile t) {
            super(Slots.values().length);
            this.tile = t;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (slot == Slots.INPUT.getId())
                tile.setChanged();
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return super.insertItem(slot, stack, simulate);
        }

    }

}
