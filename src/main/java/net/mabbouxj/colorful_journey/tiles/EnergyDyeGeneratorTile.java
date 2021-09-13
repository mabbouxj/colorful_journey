package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.capabilities.EnergyDyeGeneratorItemHandler;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.utils.EnergyUtils;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class EnergyDyeGeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        FUEL(0),
        MATERIAL(1);

        int id;

        Slots(int number) {
            id = number;
        }

        public int getId() {
            return id;
        }
    }

    private int remainingMaterial = 0;
    private int remainingFuel = 0;
    private int maxFuel = 0;

    public TileEnergyStorageCapability energyStorage;
    private LazyOptional<TileEnergyStorageCapability> energy;
    private LazyOptional<ItemStackHandler> inventory;

    public final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return EnergyDyeGeneratorTile.this.energyStorage.getEnergyStored() / 32;
                case 1:
                    return EnergyDyeGeneratorTile.this.energyStorage.getMaxEnergyStored() / 32;
                case 2:
                    return EnergyDyeGeneratorTile.this.remainingFuel;
                case 3:
                    return EnergyDyeGeneratorTile.this.maxFuel;
                case 4:
                    return EnergyDyeGeneratorTile.this.remainingMaterial;
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
            return 5;
        }
    };

    public EnergyDyeGeneratorTile() {
        super(ModTiles.ENERGY_DYE_GENERATOR.get());
        this.energyStorage = new TileEnergyStorageCapability(this, 0, ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_BUFFER_CAPACITY.get(), ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_MAX_IN_OUT.get(), true, false);
        this.energy = LazyOptional.of(() -> this.energyStorage);
        this.inventory = LazyOptional.of(() -> new EnergyDyeGeneratorItemHandler(this));
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert level != null;
        return new EnergyDyeGeneratorContainer(this, this.data, i, playerInventory, this.inventory.orElse(new ItemStackHandler(2)));
    }

    @Override
    public void tick() {
        if (this.level == null)
            return;
        inventory.ifPresent(handler -> {
            this.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {

                EnergyUtils.extractToAdjacentBlocks(this.level, (TileEnergyStorageCapability) energyStorage, getBlockPos());

                boolean canInsertEnergy = energyStorage.receiveEnergy(ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_PRODUCTION_PER_TICK.get(), true) > 0;
                if (!canInsertEnergy)
                    return;
                if (remainingFuel <= 0)
                    ignite();
                if (remainingMaterial <= 0)
                    burn();
                if (remainingFuel > 0 && remainingMaterial > 0) {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, true), 3);
                    generateEnergy(energyStorage);
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, false), 3);
                }
            });
        });
    }

    private void generateEnergy(IEnergyStorage energyStorage) {
        energyStorage.receiveEnergy(ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_PRODUCTION_PER_TICK.get(), false);
        remainingFuel--;
        remainingMaterial = remainingMaterial - ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_PRODUCTION_PER_TICK.get();
        if (remainingFuel == 0) {
            maxFuel = 0;
            ignite();
        }
        if (remainingMaterial == 0) {
            burn();
        }
    }

    private void burn() {
        ItemStackHandler handler = inventory.orElseThrow(RuntimeException::new);
        ItemStack materialStack = handler.getStackInSlot(Slots.MATERIAL.id);

        if (remainingFuel > 0 && !materialStack.isEmpty()) {
            handler.extractItem(Slots.MATERIAL.id, 1, false);
            setChanged();
            remainingMaterial = ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_PRODUCTION_PER_MATERIAL.get();
        }
    }

    private void ignite() {
        ItemStackHandler handler = inventory.orElseThrow(RuntimeException::new);
        ItemStack fuelStack = handler.getStackInSlot(Slots.FUEL.id);

        int burnTime = ForgeHooks.getBurnTime(fuelStack);
        if (burnTime > 0) {
            Item fuel = handler.getStackInSlot(Slots.FUEL.id).getItem();
            handler.extractItem(Slots.FUEL.id, 1, false);
            if(fuel instanceof BucketItem && fuel != Items.BUCKET)
                handler.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
            setChanged();
            remainingFuel = (int) (Math.floor(burnTime) / ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED.get());
            maxFuel = remainingFuel;
        }
    }

    @Override
    public void load(BlockState stateIn, CompoundNBT compound) {
        super.load(stateIn, compound);
        inventory.ifPresent(h -> h.deserializeNBT(compound.getCompound("inv")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
        remainingFuel = compound.getInt("remainingFuel");
        maxFuel = compound.getInt("maxFuel");
        remainingMaterial = compound.getInt("remainingMaterial");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inventory.ifPresent(h ->  compound.put("inv", h.serializeNBT()));
        energy.ifPresent(h -> compound.put("energy", h.serializeNBT()));
        compound.putInt("remainingFuel", remainingFuel);
        compound.putInt("maxFuel", maxFuel);
        compound.putInt("remainingMaterial", remainingMaterial);
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();

        if (cap == CapabilityEnergy.ENERGY)
            return energy.cast();

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
        super.setRemoved();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.energy_dye_generator");
    }
}
