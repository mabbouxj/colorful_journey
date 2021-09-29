package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EnergyCapacitorContainer extends BaseTileContainer {

    public EnergyCapacitorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EnergyCapacitorTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), windowId, playerInventory, new ItemStackHandler(EnergyCapacitorTile.Slots.values().length));
    }

    public EnergyCapacitorContainer(@Nullable EnergyCapacitorTile tile, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.ENERGY_CAPACITOR.get(), windowId, tile);
        addSlot(new RestrictedSlot(handler, EnergyCapacitorTile.Slots.CHARGE.getId(), 98, 35, EnergyCapacitorTile.Slots.CHARGE.getFilter()));
        bindPlayerInventory(playerInventory);
    }

}
