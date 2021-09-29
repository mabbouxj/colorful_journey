package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BooleanNBTHandler implements INBTHandler<Boolean> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return boolean.class.isAssignableFrom(aClass) || Boolean.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Boolean object) {
        compound.putBoolean(name, object);
        return true;
    }

    @Override
    public Boolean readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Boolean currentValue) {
        return compound.contains(name) ? compound.getBoolean(name) : currentValue != null && currentValue;
    }
}