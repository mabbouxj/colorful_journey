package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.annotations.Sync;
import net.mabbouxj.colorful_journey.blocks.EnergyCapacitorBlock;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.components.InventoryComponent;
import net.mabbouxj.colorful_journey.containers.EnergyCapacitorContainer;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.utils.EnergyUtils;
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
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;


public class EnergyCapacitorTile extends BasicTile implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        CHARGE(0, (stack) -> { return stack.getCapability(CapabilityEnergy.ENERGY).isPresent(); });

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
    public InventoryComponent inventoryStorage;
    private final LazyOptional<TileEnergyStorageCapability> energy;
    private final LazyOptional<ItemStackHandler> inventory;

    public EnergyCapacitorTile() {
        super(ModTiles.ENERGY_CAPACITOR.get());
        this.energyStorage = new TileEnergyStorageCapability(this, ModConfigs.COMMON.ENERGY_CAPACITOR_BUFFER_CAPACITY.get(), ModConfigs.COMMON.ENERGY_CAPACITOR_MAX_IN_OUT.get());
        this.energy = LazyOptional.of(() -> this.energyStorage);
        this.inventoryStorage = new InventoryComponent(this, Slots.values().length);
        this.inventory = LazyOptional.of(() -> inventoryStorage);
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
        setChanged();
    }

    private void chargeItem(ItemStack stack) {
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(itemEnergy -> {
            if (!isChargingItem(itemEnergy))
                return;
            int energyRemoved = itemEnergy.receiveEnergy(Math.min(energyStorage.getEnergyStored(), ModConfigs.COMMON.ENERGY_CAPACITOR_MAX_IN_OUT.get()), false);
            energyStorage.extractEnergy(energyRemoved, false);
        }));
    }

    public boolean isChargingItem(IEnergyStorage energy) {
        return energy.getEnergyStored() >= 0 && energy.receiveEnergy(energy.getEnergyStored(), true) >= 0;
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
    public void setRemoved() {
        energy.invalidate();
        inventory.invalidate();
        super.setRemoved();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.energy_capacitor");
    }

}
