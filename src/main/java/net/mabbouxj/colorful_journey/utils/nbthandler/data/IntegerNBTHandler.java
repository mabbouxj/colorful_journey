package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntegerNBTHandler implements INBTHandler<Integer> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return int.class.isAssignableFrom(aClass) || Integer.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Integer object) {
        compound.putInt(name, object);
        return true;
    }

    @Override
    public Integer readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Integer current) {
        return compound.contains(name) ? compound.getInt(name) : current == null ? 0: current;
    }
}