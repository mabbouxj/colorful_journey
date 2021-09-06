package net.mabbouxj.colorful_journey.enums;

import net.minecraft.item.DyeColor;

public enum ColorAttributesModifier {
    // white      orange  magenta light_blue
    // yellow     lime    pink    gray
    // light_gray cyan    purple  blue
    // brown      green   red     black
    HEALTH (new double[]{
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 2.0, 1.2}),
    SPEED  (new double[]{
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 1.2,
            1.2, 2.0, 1.2, 1.2}),
    DAMAGE (new double[]{
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 1.2,
            1.2, 1.2, 1.2, 2.0,
            1.2, 1.2, 1.2, 1.2}),
    ARMOR (new double[]{
            1.2, 2.0, 1.2, 1.2,
            1.2, 2.0, 1.2, 1.2,
            1.2, 2.0, 1.2, 1.2,
            1.2, 2.0, 1.2, 1.2});

    private final double[] colors;

    ColorAttributesModifier(double[] colors) {
        this.colors = colors;
    }

    public double byColor(DyeColor color) {
        if (colors.length >= color.getId()) {
            return colors[color.getId()];
        }
        return 1.0D;
    }
}
