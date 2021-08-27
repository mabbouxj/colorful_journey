package net.mabbouxj.colorful_journey.entities;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public interface IColoredMobEntity {

    DyeColor getColor();

    void setColor(DyeColor color);

    EntityModel getEntityModel();

    ResourceLocation getLayerLocation();

}
