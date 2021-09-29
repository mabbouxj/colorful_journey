package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.mabbouxj.colorful_journey.utils.nbthandler.INBTHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackHandlerNBTHandler implements INBTHandler<ItemStackHandler> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return ItemStackHandler.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull ItemStackHandler object) {
        compound.put(name, object.serializeNBT());
        return true;
    }

    @Override
    public ItemStackHandler readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable ItemStackHandler current) {
        if (compound.contains(name)) {
            if (current == null) current = new ItemStackHandler();
            current.deserializeNBT(compound.getCompound(name));
            return current;
        }
        return current;
    }
}