package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.tiles.EaselTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EaselContainer extends BaseTileContainer {

    protected EaselTile tile;

    public EaselContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EaselTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), windowId, playerInventory, new ItemStackHandler(EaselTile.Slots.values().length));
    }

    public EaselContainer(@Nullable EaselTile tile, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.EASEL.get(), windowId, tile);
        this.tile = tile;
        addSlot(new RestrictedSlot(handler, EaselTile.Slots.PALETTE.getId(), 8, 71, EaselTile.Slots.PALETTE.getFilter()));
        bindPlayerInventory(playerInventory);
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 102;
    }

    public boolean hasSlate() {
        return this.tile.hasSlate;
    }

    public int getSelectedColor() {
        return this.tile.selectedColor;
    }

    public void setSelectedColor(int colorId) {
        this.tile.selectedColor = colorId;
        this.tile.setChanged();
    }

    public int[] getPatternToReproduce() {
        return this.tile.patternToReproduce;
    }

    public int[] getPatternBeingPainted() {
        return this.tile.patternBeingPainted;
    }

    public void setPatternBeingPaintedAndDamageColorPalette(int[] pattern, int damage) {
        this.tile.patternBeingPainted = pattern;
        ItemStack stack = this.tile.inventoryStorage.getStackInSlot(EaselTile.Slots.PALETTE.getId());
        if (stack.getItem().equals(ModItems.COLOR_PALETTE.get())) {
            stack.setDamageValue(stack.getDamageValue() + damage);
        }
        this.tile.setChanged();
    }

    public boolean hasUsableColorPalette() {
        ItemStack stack = this.tile.inventoryStorage.getStackInSlot(EaselTile.Slots.PALETTE.getId());
        return stack.getItem().equals(ModItems.COLOR_PALETTE.get()) && stack.getDamageValue() < stack.getMaxDamage();
    }

    public boolean isFinished() {
        return this.tile.isFinished();
    }

}
