package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.annotations.Sync;
import net.mabbouxj.colorful_journey.blocks.WashingMachineBlock;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.capabilities.TileFluidStorageCapability;
import net.mabbouxj.colorful_journey.components.InventoryComponent;
import net.mabbouxj.colorful_journey.containers.WashingMachineContainer;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.mabbouxj.colorful_journey.utils.RecipeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
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
import java.util.function.Predicate;

public class WashingMachineTile extends BasicTile implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        INPUT(0, (stack) -> true),
        OUTPUT1(1, (stack) -> false),
        OUTPUT2(2, (stack) -> false);

        int id;
        Predicate<ItemStack> filter;

        Slots(int number, Predicate<ItemStack> filter) {
            this.id = number;
            this.filter = filter;
        }

        public int getId() {
            return id;
        }
        public Predicate<ItemStack> getFilter() {
            return filter;
        }
    }

    @Sync
    public TileEnergyStorageCapability energyStorage;
    @Sync
    public TileFluidStorageCapability fluidStorage;
    @Sync
    public InventoryComponent inventoryStorage;
    private final LazyOptional<TileEnergyStorageCapability> energy;
    private final LazyOptional<TileFluidStorageCapability> fluid;
    private final LazyOptional<ItemStackHandler> inventory;

    @Sync
    public int recipeProgress = 0;
    @Sync
    public int recipeMaxProgress = 0;
    public WashingMachineRecipe currentRecipe;

    public WashingMachineTile() {
        super(ModTiles.WASHING_MACHINE.get());
        this.energyStorage = new TileEnergyStorageCapability(this, ModConfigs.COMMON.WASHING_MACHINE_ENERGY_BUFFER.get(), 1000, false, true);
        this.energy = LazyOptional.of(() -> energyStorage);
        this.fluidStorage = new TileFluidStorageCapability(this, ModConfigs.COMMON.WASHING_MACHINE_FLUID_BUFFER.get());
        this.fluid = LazyOptional.of(() -> fluidStorage);
        this.inventoryStorage = new InventoryComponent(this, Slots.values().length);
        this.inventory = LazyOptional.of(() -> inventoryStorage);
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
                recipeMaxProgress = 0;
            } else {
                recipeMaxProgress = currentRecipe.getProcessingTime();
            }
        })));
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
        assert level != null;
        return new WashingMachineContainer(this, i, playerInventory, this.inventory.orElse(new ItemStackHandler(Slots.values().length)));
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

            if (recipeProgress >= currentRecipe.getProcessingTime()) {
                inventory.insertItem(Slots.OUTPUT1.getId(), recipeResult, false);
                inventory.insertItem(Slots.OUTPUT2.getId(), recipeResultAlt, false);
                inventory.getStackInSlot(Slots.INPUT.getId()).shrink(currentRecipe.getInputStack().getCount());
                tank.drain(currentRecipe.getInputFluid(), IFluidHandler.FluidAction.EXECUTE);
                recipeProgress = 0;
                recipeMaxProgress = 0;
                currentRecipe = null;
            }

            setChanged();
        })));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
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
    public void setRemoved() {
        energy.invalidate();
        inventory.invalidate();
        fluid.invalidate();
        super.setRemoved();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.washing_machine");
    }

}
