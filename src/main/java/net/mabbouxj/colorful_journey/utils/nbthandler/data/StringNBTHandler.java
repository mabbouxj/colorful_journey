package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringNBTHandler implements INBTHandler<String> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return String.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull String object) {
        compound.putString(name, object);
        return true;
    }

    @Override
    public String readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable String current) {
        return compound.contains(name) ? compound.getString(name) : current;
    }
}