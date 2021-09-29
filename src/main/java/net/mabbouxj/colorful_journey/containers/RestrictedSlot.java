package net.mabbouxj.colorful_journey.containers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class RestrictedSlot extends SlotItemHandler {

    Predicate<ItemStack> filter;

    public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(itemHandler, index, xPosition, yPosition, null);
    }

    public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> filter) {
        super(itemHandler, index, xPosition, yPosition);
        this.filter = filter;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if (this.filter == null)
            return true;
        return this.filter.test(stack);
    }

}
