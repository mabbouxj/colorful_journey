package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DoubleNBTHandler implements INBTHandler<Double> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return double.class.isAssignableFrom(aClass) || Double.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Double object) {
        compound.putDouble(name, object);
        return true;
    }

    @Override
    public Double readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Double currentValue) {
        return compound.contains(name) ? compound.getDouble(name) : currentValue == null ? 0: currentValue;
    }
}