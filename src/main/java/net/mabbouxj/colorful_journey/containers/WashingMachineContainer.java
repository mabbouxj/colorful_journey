package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WashingMachineContainer extends BaseTileContainer {

    private static final int SLOTS = WashingMachineTile.Slots.values().length;
    public final IIntArray data;

    public WashingMachineContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((WashingMachineTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(2), windowId, playerInventory, new ItemStackHandler(SLOTS));
    }

    public WashingMachineContainer(@Nullable WashingMachineTile tile, IIntArray data, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.WASHING_MACHINE.get(), windowId, tile);
        this.tile = tile;
        this.data = data;

        addSlot(new WashingMachineContainer.RestrictedSlot(handler, WashingMachineTile.Slots.INPUT.getId(), 62, 35));
        addSlot(new WashingMachineContainer.RestrictedSlot(handler, WashingMachineTile.Slots.OUTPUT1.getId(), 116, 35));
        addSlot(new WashingMachineContainer.RestrictedSlot(handler, WashingMachineTile.Slots.OUTPUT2.getId(), 134, 35));
        bindPlayerInventory(playerInventory);
        addDataSlots(data);
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getMaxProgress() {
        return this.data.get(1);
    }

    @Override
    protected int getMergeableSlotCount() {
        return tile == null ? 0 : SLOTS;
    }

    static class RestrictedSlot extends SlotItemHandler {
        public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if( getSlotIndex() == WashingMachineTile.Slots.INPUT.getId() )
                return true;
            if( getSlotIndex() == WashingMachineTile.Slots.OUTPUT1.getId() )
                return false;
            if( getSlotIndex() == WashingMachineTile.Slots.OUTPUT2.getId() )
                return false;
            return super.mayPlace(stack);
        }
    }

}
