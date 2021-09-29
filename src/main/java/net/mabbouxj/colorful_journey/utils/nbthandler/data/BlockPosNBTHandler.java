package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPosNBTHandler implements INBTHandler<BlockPos> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return BlockPos.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull BlockPos object) {
        compound.putLong(name, object.asLong());
        return false;
    }

    @Override
    public BlockPos readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable BlockPos current) {
        return compound.contains(name) ? BlockPos.of(compound.getLong(name)) : current;
    }
}

