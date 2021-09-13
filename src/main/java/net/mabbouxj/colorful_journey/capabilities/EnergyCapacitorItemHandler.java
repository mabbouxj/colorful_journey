package net.mabbouxj.colorful_journey.capabilities;

import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnergyCapacitorItemHandler extends ItemStackHandler {

    private final EnergyCapacitorTile tile;

    public EnergyCapacitorItemHandler(EnergyCapacitorTile t) {
        super(1);
        this.tile = t;
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.setChanged();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == EnergyCapacitorTile.Slots.CHARGE.getId() && (! stack.getCapability(CapabilityEnergy.ENERGY).isPresent() || getStackInSlot(slot).getCount() > 0))
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

}
