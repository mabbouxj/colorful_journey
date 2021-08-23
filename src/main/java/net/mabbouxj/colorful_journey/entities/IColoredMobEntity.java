package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface IColoredMobEntity {

    DyeColor getColor();

    void setColor(DyeColor color);

    ColorfulItem getReplacementItemFor(Item item);

    EntityModel getEntityModel();

    ResourceLocation getLayerLocation();

}
