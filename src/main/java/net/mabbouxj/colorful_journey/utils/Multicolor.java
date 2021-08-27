package net.mabbouxj.colorful_journey.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;
import java.awt.*;

public class Multicolor {

    public static class Item implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int tint) {
            if (tint == 0) {
                return ColorUtils.getColor(itemStack).getColorValue();
            }
            return Color.WHITE.getRGB();
        }
    }

    public static class Block implements IBlockColor {
        @Override
        public int getColor(BlockState blockState, @Nullable IBlockDisplayReader blockDisplayReader, @Nullable BlockPos blockPos, int tint) {
            if (tint == 0) {
                int colorId = ColorUtils.getColorId(blockState);
                return DyeColor.byId(colorId).getColorValue();
            }
            return Color.WHITE.getRGB();
        }
    }

}
