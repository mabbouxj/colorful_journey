package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class UUIDNBTHandler implements INBTHandler<UUID> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return UUID.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull UUID object) {
        compound.putUUID(name, object);
        return true;
    }

    @Override
    public UUID readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable UUID current) {
        return compound.contains(name) ? compound.getUUID(name) : current;
    }
}