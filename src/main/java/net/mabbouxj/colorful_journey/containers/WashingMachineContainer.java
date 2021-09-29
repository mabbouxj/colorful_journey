package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class WashingMachineContainer extends BaseTileContainer {

    protected WashingMachineTile tile;

    public WashingMachineContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((WashingMachineTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), windowId, playerInventory, new ItemStackHandler(WashingMachineTile.Slots.values().length));
    }

    public WashingMachineContainer(@Nullable WashingMachineTile tile, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.WASHING_MACHINE.get(), windowId, tile);
        this.tile = tile;

        addSlot(new RestrictedSlot(handler, WashingMachineTile.Slots.INPUT.getId(), 62, 35, WashingMachineTile.Slots.INPUT.getFilter()));
        addSlot(new RestrictedSlot(handler, WashingMachineTile.Slots.OUTPUT1.getId(), 116, 35, WashingMachineTile.Slots.OUTPUT1.getFilter()));
        addSlot(new RestrictedSlot(handler, WashingMachineTile.Slots.OUTPUT2.getId(), 134, 35, WashingMachineTile.Slots.OUTPUT2.getFilter()));

        bindPlayerInventory(playerInventory);
    }

    public int getProgress() {
        return this.tile.recipeProgress;
    }

    public int getMaxProgress() {
        return this.tile.recipeMaxProgress;
    }

}
