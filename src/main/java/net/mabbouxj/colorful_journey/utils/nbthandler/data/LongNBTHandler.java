package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LongNBTHandler implements INBTHandler<Long> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return long.class.isAssignableFrom(aClass) || Long.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Long object) {
        compound.putLong(name, object);
        return true;
    }

    @Override
    public Long readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Long current) {
        return compound.contains(name) ? compound.getLong(name) : current == null ? 0: current;
    }
}