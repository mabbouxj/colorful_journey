package net.mabbouxj.colorful_journey.utils;

public class StringUtils {

    public static String numberWithSuffix(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public static String ticksInHumanReadable(int ticks) {
        float seconds = ticks / 20f;
        if (seconds < 60)
            return String.format("%.2f", seconds) + "s";
        float minutes = seconds / 60f;
        return String.format("%.2f", minutes) + "min";
    }

}
