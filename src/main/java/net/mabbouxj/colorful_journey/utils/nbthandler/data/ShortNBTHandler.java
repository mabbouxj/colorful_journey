package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShortNBTHandler implements INBTHandler<Short> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return short.class.isAssignableFrom(aClass) || Short.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull Short object) {
        compound.putShort(name, object);
        return true;
    }

    @Override
    public Short readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable Short current) {
        return compound.contains(name) ? compound.getShort(name) : current == null ? 0: current;
    }
}