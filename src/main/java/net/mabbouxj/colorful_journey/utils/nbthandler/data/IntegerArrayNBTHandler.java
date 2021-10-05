package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntegerArrayNBTHandler implements INBTHandler<int[]> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return int[].class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull int[] object) {
        compound.putIntArray(name, object);
        return true;
    }

    @Override
    public int[] readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable int[] currentValue) {
        return compound.contains(name) ? compound.getIntArray(name) : currentValue == null ? new int[0]: currentValue;
    }
}
