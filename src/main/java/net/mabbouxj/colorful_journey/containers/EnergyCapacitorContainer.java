package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyCapacitorContainer extends BaseTileContainer {

    private static final int SLOTS = EnergyCapacitorTile.Slots.values().length;

    public EnergyCapacitorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EnergyCapacitorTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), windowId, playerInventory, new ItemStackHandler(SLOTS));
    }

    public EnergyCapacitorContainer(@Nullable EnergyCapacitorTile tile, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.ENERGY_CAPACITOR.get(), windowId, tile);

        addSlot(new RestrictedSlot(handler, EnergyCapacitorTile.Slots.CHARGE.getId(), 98, 35));
        bindPlayerInventory(playerInventory);
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
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if(getSlotIndex() == EnergyCapacitorTile.Slots.CHARGE.getId())
                return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
            return super.mayPlace(stack);
        }
    }

}
