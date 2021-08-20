package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.Reference;
import net.minecraft.item.DyeColor;

import java.util.Arrays;
import java.util.Random;

public class ColorUtils {

    public static DyeColor getRandomDyeColor() {
        return Arrays.asList(Reference.COLORS).get(new Random().nextInt(Reference.COLORS.length));
    }

    public static float[] createColor(DyeColor dyeColor) {
        if (dyeColor == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = dyeColor.getTextureDiffuseColors();
            float f = 0.75F;
            return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
        }
    }

}
