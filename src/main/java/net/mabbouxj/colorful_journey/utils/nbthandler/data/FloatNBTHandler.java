package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FloatNBTHandler implements INBTHandler<Float> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return float.class.isAssignableFrom(aClass) || Float.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Float object) {
        compound.putFloat(name, object);
        return true;
    }

    @Override
    public Float readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Float currentValue) {
        return compound.contains(name) ? compound.getFloat(name) : currentValue == null ? 0: currentValue;
    }
}