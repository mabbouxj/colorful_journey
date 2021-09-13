package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.capabilities.EnergyCapacitorItemHandler;
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
import net.minecraft.util.IIntArray;
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
    private LazyOptional<TileEnergyStorageCapability> energy;
    private LazyOptional<ItemStackHandler> inventory;

    public final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return EnergyCapacitorTile.this.energyStorage.getEnergyStored() / 32;
                case 1:
                    return EnergyCapacitorTile.this.energyStorage.getMaxEnergyStored() / 32;
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

    public EnergyCapacitorTile() {
        super(ModTiles.ENERGY_CAPACITOR.get());
        this.energyStorage = new TileEnergyStorageCapability(this, 0, ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_BUFFER_CAPACITY.get(), ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_MAX_IN_OUT.get());
        this.energy = LazyOptional.of(() -> this.energyStorage);
        this.inventory = LazyOptional.of(() -> new EnergyCapacitorItemHandler(this));
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert level != null;
        return new EnergyCapacitorContainer(this, this.data, i, playerInventory, this.inventory.orElse(new ItemStackHandler(1)));
    }

    @Override
    public void tick() {
        if (getLevel() == null)
            return;
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
            EnergyUtils.extractFromAdjacentBlocks(this.level, (TileEnergyStorageCapability) energyStorage, this.getBlockPos());
            EnergyUtils.extractToAdjacentBlocks(this.level, (TileEnergyStorageCapability) energyStorage, this.getBlockPos());
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
}
