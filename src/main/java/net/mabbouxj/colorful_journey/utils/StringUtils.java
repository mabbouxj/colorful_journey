package net.mabbouxj.colorful_journey.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtils {

    public static String numberWithSuffix(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public static String ticksInSeconds(int ticks) {
        BigDecimal TWENTY = new BigDecimal(20);
        BigDecimal value = new BigDecimal(ticks);
        value = value.divide(TWENTY, 1, RoundingMode.HALF_UP);
        return value.toString();
    }

}
