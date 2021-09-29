package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NBTSerializableNBTHandler implements INBTHandler<INBTSerializable> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return INBTSerializable.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull INBTSerializable object) {
        compound.put(name, object.serializeNBT());
        return false;
    }

    @Override
    public INBTSerializable readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable INBTSerializable currentValue) {
        if (compound.contains(name) && currentValue != null) {
            currentValue.deserializeNBT(compound.get(name));
            return currentValue;
        }
        return currentValue;
    }
}