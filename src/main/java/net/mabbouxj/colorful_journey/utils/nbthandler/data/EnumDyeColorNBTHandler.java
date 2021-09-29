package net.mabbouxj.colorful_journey.utils.nbthandler.data;

import net.minecraft.item.DyeColor;

public class EnumDyeColorNBTHandler extends EnumNBTHandler<DyeColor> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return DyeColor.class.isAssignableFrom(aClass);
    }

    @Override
    protected int getIdFrom(DyeColor obj) {
        return obj.getId();
    }

    @Override
    protected DyeColor getFromId(int id) {
        return DyeColor.byId(id);
    }
}