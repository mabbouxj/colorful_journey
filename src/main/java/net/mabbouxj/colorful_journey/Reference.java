package net.mabbouxj.colorful_journey;

import com.google.common.collect.Maps;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Reference {
    public static final String MOD_ID = "colorful_journey";
    public static final String MOD_NAME = "Colorful Journey";
    public static final String MOD_VERSION = "0.0.1";
    public static final ItemGroup MOD_ITEM_GROUP = new ColorfulJourneyItemGroup(MOD_ID);

    public static final DyeColor[] COLORS = new DyeColor[]{
            DyeColor.RED,
            DyeColor.GREEN,
            DyeColor.BLUE
    };
    public static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR = Maps.newEnumMap(Arrays.stream(COLORS).collect(Collectors.toMap((DyeColor dyeColor) -> {
        return dyeColor;
    }, ColorUtils::createColor)));

    public static class ColorfulJourneyItemGroup extends ItemGroup {
        public ColorfulJourneyItemGroup(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.INK_BALL.get());
        }
    }
}
