package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class CustomItemColor implements IItemColor {

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {

        if (tintIndex == 0) {
            return ColorfulItem.getColor(itemStack).getColorValue();
        }

        return Color.WHITE.getRGB();
    }

}
