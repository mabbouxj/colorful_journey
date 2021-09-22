package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyDyeGeneratorContainer extends BaseTileContainer {

    private static final int SLOTS = EnergyDyeGeneratorTile.Slots.values().length;
    public final IIntArray data;

    public EnergyDyeGeneratorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EnergyDyeGeneratorTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(3), windowId, playerInventory, new ItemStackHandler(SLOTS));
    }

    public EnergyDyeGeneratorContainer(@Nullable EnergyDyeGeneratorTile tile, IIntArray data, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.ENERGY_DYE_GENERATOR.get(), windowId, tile);
        this.data = data;

        addSlot(new RestrictedSlot(handler, EnergyDyeGeneratorTile.Slots.FUEL.getId(), 62, 53));
        addSlot(new RestrictedSlot(handler, EnergyDyeGeneratorTile.Slots.MATERIAL.getId(), 62, 17));
        bindPlayerInventory(playerInventory);
        addDataSlots(data);
    }

    public int getRemainingFuel() {
        return this.data.get(0);
    }

    public int getMaxFuel() {
        return this.data.get(1);
    }

    public int getRemainingMaterial() {
        return this.data.get(2);
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
            if( getSlotIndex() == EnergyDyeGeneratorTile.Slots.MATERIAL.getId() )
                return stack.getItem().is(Tags.Items.DYES);

            if( getSlotIndex() == EnergyDyeGeneratorTile.Slots.FUEL.getId() )
                return ForgeHooks.getBurnTime(stack, null) != 0;

            return super.mayPlace(stack);
        }
    }

}
