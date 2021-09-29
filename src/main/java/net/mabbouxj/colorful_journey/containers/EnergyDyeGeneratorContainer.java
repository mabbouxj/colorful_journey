package net.mabbouxj.colorful_journey.containers;

import net.mabbouxj.colorful_journey.init.ModContainers;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EnergyDyeGeneratorContainer extends BaseTileContainer {

    protected EnergyDyeGeneratorTile tile;

    public EnergyDyeGeneratorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((EnergyDyeGeneratorTile) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), windowId, playerInventory, new ItemStackHandler(EnergyDyeGeneratorTile.Slots.values().length));
    }

    public EnergyDyeGeneratorContainer(@Nullable EnergyDyeGeneratorTile tile, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModContainers.ENERGY_DYE_GENERATOR.get(), windowId, tile);
        this.tile = tile;

        addSlot(new RestrictedSlot(handler, EnergyDyeGeneratorTile.Slots.FUEL.getId(), 62, 53, EnergyDyeGeneratorTile.Slots.FUEL.getFilter()));
        addSlot(new RestrictedSlot(handler, EnergyDyeGeneratorTile.Slots.INGREDIENT.getId(), 62, 17, EnergyDyeGeneratorTile.Slots.INGREDIENT.getFilter()));

        bindPlayerInventory(playerInventory);
    }

    public int getRemainingFuel() {
        return this.tile.remainingFuel;
    }

    public int getMaxFuel() {
        return this.tile.maxFuel;
    }

    public int getRemainingIngredient() {
        return this.tile.remainingIngredient;
    }

    public int getCurrentEnergyGeneration() {
        return this.tile.generating;
    }

}
