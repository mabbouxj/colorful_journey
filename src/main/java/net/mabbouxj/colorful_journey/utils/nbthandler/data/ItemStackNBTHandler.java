package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackNBTHandler implements INBTHandler<ItemStack> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return ItemStack.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull ItemStack object) {
        compound.put(name, object.serializeNBT());
        return true;
    }

    @Override
    public ItemStack readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable ItemStack currentValue) {
        return ItemStack.of(compound.getCompound(name));
    }
}