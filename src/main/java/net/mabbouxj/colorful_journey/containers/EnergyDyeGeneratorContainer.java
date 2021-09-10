package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyDyeGeneratorContainer extends Container {

    private static final int SLOTS = 2;
    public final IIntArray data;
    public ItemStackHandler handler;
    private final EnergyDyeGeneratorTile tile;

    public EnergyDyeGeneratorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EnergyDyeGeneratorTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(5), windowId, playerInventory, new ItemStackHandler(2));
    }

    public EnergyDyeGeneratorContainer(@Nullable EnergyDyeGeneratorTile tile, IIntArray data, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.ENERGY_DYE_GENERATOR.get(), windowId);

        this.handler = handler;
        this.tile = tile;

        this.data = data;
        this.setup(playerInventory);

        addDataSlots(data);
    }

    public void setup(PlayerInventory inventory) {
        addSlot(new RestrictedSlot(handler, 0, 62, 56));
        addSlot(new RestrictedSlot(handler, 1, 62, 21));

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 143;
            addSlot(new Slot(inventory, row, x, y));
        }
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 85 + row * 18;
                addSlot(new Slot(inventory, 9 + col + row * 9, x, y));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack currentStack = slot.getItem();
            itemstack = currentStack.copy();

            if (index < SLOTS) {
                if (! this.moveItemStackTo(currentStack, SLOTS, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (! this.moveItemStackTo(currentStack, 0, SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (currentStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int fromIndex, int toIndex, boolean simulate) {
        return super.moveItemStackTo(stack, fromIndex, toIndex, simulate);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        BlockPos pos = this.tile.getBlockPos();
        return !this.tile.isRemoved() && player.distanceToSqr(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public int getEnergy() {
        return this.data.get(0) * 32;
    }

    public int getMaxPower() {
        return this.data.get(1) * 32;
    }

    public int getRemainingFuel() {
        return this.data.get(2);
    }

    public int getMaxFuel() {
        return this.data.get(3);
    }

    public int getRemainingMaterial() {
        return this.data.get(4);
    }

    static class RestrictedSlot extends SlotItemHandler {
        public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if( getSlotIndex() == EnergyDyeGeneratorTile.Slots.MATERIAL.getId() )
                return stack.getItem().is(Tags.Items.DYES);

            if( getSlotIndex() == EnergyDyeGeneratorTile.Slots.FUEL.getId() )
                return ForgeHooks.getBurnTime(stack, null) != 0;

            return super.mayPlace(stack);
        }
    }

}
