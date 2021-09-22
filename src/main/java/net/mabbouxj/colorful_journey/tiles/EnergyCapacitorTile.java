package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.blocks.EnergyCapacitorBlock;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.containers.EnergyCapacitorContainer;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.utils.EnergyUtils;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class EnergyCapacitorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        CHARGE(0);

        int id;

        Slots(int number) {
            id = number;
        }

        public int getId() {
            return id;
        }
    }

    public TileEnergyStorageCapability energyStorage;
    private final LazyOptional<TileEnergyStorageCapability> energy;
    private final LazyOptional<ItemStackHandler> inventory;

    public EnergyCapacitorTile() {
        super(ModTiles.ENERGY_CAPACITOR.get());
        this.energyStorage = new TileEnergyStorageCapability(this, ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_BUFFER_CAPACITY.get(), ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_MAX_IN_OUT.get());
        this.energy = LazyOptional.of(() -> this.energyStorage);
        this.inventory = LazyOptional.of(() -> new EnergyCapacitorTile.ItemHandler(this));
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert level != null;
        return new EnergyCapacitorContainer(this, i, playerInventory, this.inventory.orElse(new ItemStackHandler(1)));
    }

    @Override
    public void tick() {
        if (this.level == null)
            return;
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
            EnergyUtils.extractFromAdjacentBlocks(this.level, (TileEnergyStorageCapability) energyStorage, this.getBlockPos());
            EnergyUtils.extractToAdjacentBlocks(this.level, (TileEnergyStorageCapability) energyStorage, this.getBlockPos());
            if (energyStorage.getEnergyStored() < 0.1 * energyStorage.getMaxEnergyStored()) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyCapacitorBlock.FILL, 0), 3);
            } else if (energyStorage.getEnergyStored() < 0.35 * energyStorage.getMaxEnergyStored()) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyCapacitorBlock.FILL, 1), 3);
            } else if (energyStorage.getEnergyStored() < 0.65 * energyStorage.getMaxEnergyStored()) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyCapacitorBlock.FILL, 2), 3);
            } else if (energyStorage.getEnergyStored() < 0.95 * energyStorage.getMaxEnergyStored()) {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyCapacitorBlock.FILL, 3), 3);
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyCapacitorBlock.FILL, 4), 3);
            }
        });
        inventory.ifPresent(handler -> {
            ItemStack stack = handler.getStackInSlot(Slots.CHARGE.id);
            if (!stack.isEmpty())
                chargeItem(stack);
        });
    }

    private void chargeItem(ItemStack stack) {
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(itemEnergy -> {
            if (!isChargingItem(itemEnergy))
                return;
            int energyRemoved = itemEnergy.receiveEnergy(Math.min(energyStorage.getEnergyStored(), ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_MAX_IN_OUT.get()), false);
            energyStorage.extractEnergy(energyRemoved, false);
        }));
    }

    public boolean isChargingItem(IEnergyStorage energy) {
        return energy.getEnergyStored() >= 0 && energy.receiveEnergy(energy.getEnergyStored(), true) >= 0;
    }

    @Override
    public void load(BlockState stateIn, CompoundNBT compound) {
        super.load(stateIn, compound);
        inventory.ifPresent(h -> h.deserializeNBT(compound.getCompound("inv")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inventory.ifPresent(h ->  compound.put("inv", h.serializeNBT()));
        energy.ifPresent(h -> compound.put("energy", h.serializeNBT()));
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
        return new TranslationTextComponent("container.colorful_journey.energy_capacitor");
    }

    public static class ItemHandler extends ItemStackHandler {

        private final EnergyCapacitorTile tile;

        public ItemHandler(EnergyCapacitorTile t) {
            super(Slots.values().length);
            this.tile = t;
        }

        @Override
        protected void onContentsChanged(int slot) {
            tile.setChanged();
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot == EnergyCapacitorTile.Slots.CHARGE.getId() && (! stack.getCapability(CapabilityEnergy.ENERGY).isPresent() || getStackInSlot(slot).getCount() > 0))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

    }
}
