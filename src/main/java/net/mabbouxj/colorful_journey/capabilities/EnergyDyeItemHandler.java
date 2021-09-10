package net.mabbouxj.colorful_journey.capabilities;

import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnergyDyeItemHandler extends ItemStackHandler {

    private final EnergyDyeGeneratorTile tile;

    public EnergyDyeItemHandler(EnergyDyeGeneratorTile t) {
        super(2);
        this.tile = t;
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.setChanged();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == EnergyDyeGeneratorTile.Slots.FUEL.getId() && stack.getItem() == Items.BUCKET)
            return super.insertItem(slot, stack, simulate);

        if (slot == EnergyDyeGeneratorTile.Slots.FUEL.getId() && ForgeHooks.getBurnTime(stack) <= 0)
            return stack;

        if (slot == EnergyDyeGeneratorTile.Slots.MATERIAL.getId() && (! stack.getCapability(CapabilityEnergy.ENERGY).isPresent() || getStackInSlot(slot).getCount() > 0))
            return stack;

        return super.insertItem(slot, stack, simulate);
    }

}
