package net.mabbouxj.colorful_journey.utils;

import net.minecraft.util.text.TranslationTextComponent;

public class StringUtils {

    public static String numberWithSuffix(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public static String ticksInHumanReadable(int ticks) {
        int millis = ticks * 50;
        if (millis < 1000)
            return new TranslationTextComponent("screen.colorful_journey.suffix_millis", String.format("%d", millis)).getString();
        float seconds = millis / 1000f;
        if (seconds < 60)
            return new TranslationTextComponent("screen.colorful_journey.suffix_second", String.format("%.2f", seconds)).getString();
        float minutes = seconds / 60f;
        return new TranslationTextComponent("screen.colorful_journey.suffix_minute", String.format("%.2f", minutes)).getString();
    }

}
