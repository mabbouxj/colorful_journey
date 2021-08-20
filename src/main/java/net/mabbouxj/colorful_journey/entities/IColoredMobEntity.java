package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;

public interface IColoredMobEntity {

    DyeColor getColor();

    void setColor(DyeColor color);

    ColorfulItem getReplacementItemFor(Item item);

}
