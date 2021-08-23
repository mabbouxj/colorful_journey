package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.item.DyeColor;

import java.util.Arrays;
import java.util.Random;

public class ColorUtils {

    public static DyeColor getRandomDyeColor() {
        return Arrays.asList(ColorfulJourney.COLORS).get(new Random().nextInt(ColorfulJourney.COLORS.length));
    }

}
