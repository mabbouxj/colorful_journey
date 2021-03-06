package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.tiles.BasicTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseTileContainer extends Container {

    protected BasicTile tile;

    protected BaseTileContainer(@Nullable ContainerType<?> containerType, int windowId, BasicTile tile) {
        super(containerType, windowId);
        this.tile = tile;
    }

    protected void bindPlayerInventory(PlayerInventory inventory) {
        int xOffset = getPlayerInventoryXOffset();
        int yOffset = getPlayerInventoryYOffset();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inventory, i, xOffset + i * 18, yOffset + 58));
        }
    }

    protected int getPlayerInventoryXOffset() {
        return 8;
    }

    protected int getPlayerInventoryYOffset() {
        return 85;
    }

    protected IEnergyStorage getEnergyCapability() {
        return this.tile.getCapability(CapabilityEnergy.ENERGY).orElse(new EnergyStorage(0));
    }

    protected IFluidHandler getFluidCapability() {
        return this.tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(new VoidFluidHandler());
    }

    public int getEnergy() {
        return getEnergyCapability().getEnergyStored();
    }

    public int getMaxEnergy() {
        return getEnergyCapability().getMaxEnergyStored();
    }

    public int getFluid() {
        return getFluidStack().getAmount();
    }

    public int getMaxFluid() {
        return getFluidCapability().getTankCapacity(0);
    }

    public FluidStack getFluidStack() {
        return getFluidCapability().getFluidInTank(0);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        BlockPos pos = this.tile.getBlockPos();
        return !this.tile.isRemoved() && player.distanceToSqr(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int containerSlots = slots.size() - player.inventory.items.size();

            if (index < containerSlots) {
                if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void slotsChanged(@Nonnull IInventory inventory) {
        super.slotsChanged(inventory);
        this.tile.setChanged();
    }


}
