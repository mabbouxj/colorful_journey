package net.mabbouxj.colorful_journey.utils;

import joptsimple.internal.Strings;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.IColoredBlock;
import net.mabbouxj.colorful_journey.items.ColoredVariantsBlockItem;
import net.mabbouxj.colorful_journey.items.ColoredVariantsItem;
import net.mabbouxj.colorful_journey.items.ColoredVariantsWallOrFloorItem;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorUtils {

    public static final String NBT_TAG_NAME_COLOR = "color";
    public static final Map<Integer, String> DYE_COLOR_TO_TEXT_FORMAT = new HashMap<Integer, String>(){{
        put(DyeColor.BLACK.getId(), "§0");
        put(DyeColor.GRAY.getId(), "§8");
        put(DyeColor.LIGHT_GRAY.getId(), "§7");
        put(DyeColor.WHITE.getId(), "§f");
        put(DyeColor.YELLOW.getId(), "§e");
        put(DyeColor.ORANGE.getId(), "§6");
        put(DyeColor.RED.getId(), "§c");
        put(DyeColor.PURPLE.getId(), "§5");
        put(DyeColor.CYAN.getId(), "§b");
        put(DyeColor.MAGENTA.getId(), "§d");
        put(DyeColor.LIME.getId(), "§a");
        put(DyeColor.GREEN.getId(), "§2");
        put(DyeColor.BLUE.getId(), "§1");
        put(DyeColor.LIGHT_BLUE.getId(), "§9");
        put(DyeColor.PINK.getId(), "§d");
        put(DyeColor.BROWN.getId(), "§4");
    }};

    public static DyeColor getRandomDyeColor() {
        return Arrays.asList(ColorfulJourney.COLORS).get(new Random().nextInt(ColorfulJourney.COLORS.length));
    }

    public static DyeColor getColor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ColoredVariantsItem) {
            return ((ColoredVariantsItem) itemStack.getItem()).getColor();
        } else if (itemStack.getItem() instanceof ColoredVariantsBlockItem) {
            return ((ColoredVariantsBlockItem) itemStack.getItem()).getColor();
        } else if (itemStack.getItem() instanceof ColoredVariantsWallOrFloorItem) {
            return ((ColoredVariantsWallOrFloorItem) itemStack.getItem()).getColor();
        }
        return DyeColor.WHITE;
    }

    public static DyeColor getColor(BlockState blockState) {
        if (blockState.getBlock() instanceof IColoredBlock) {
            return ((IColoredBlock) blockState.getBlock()).getColor();
        }
        return DyeColor.WHITE;
    }

    public static String removeColorSuffix(String str) {
        String[] splitted = str.split("_");
        return Strings.join(Arrays.copyOf(splitted, splitted.length - 1), "_");
    }

}
