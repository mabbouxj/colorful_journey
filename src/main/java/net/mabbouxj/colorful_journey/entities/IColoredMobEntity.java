package net.mabbouxj.colorful_journey.entities;

import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public interface IColoredMobEntity {

    DyeColor getColor();

    void setColor(DyeColor color);

    ResourceLocation getLayerLocation();

}
