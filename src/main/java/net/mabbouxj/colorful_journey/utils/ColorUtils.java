package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class ColorUtils {

    public static final String NBT_TAG_COLOR = "color";
    public static final Map<Integer, String> DYE_COLOR_TO_TEXT_FORMAT = new HashMap<Integer, String>(){{
        put(DyeColor.BLACK.getId(), "§f");
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

    public static DyeColor getRandomEnabledColor() {
        return (DyeColor) ColorfulJourney.ENABLED_COLORS.toArray()[new Random().nextInt(ColorfulJourney.ENABLED_COLORS.size())];
    }

    public static DyeColor getColor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IColored) {
            return ((IColored) itemStack.getItem()).getColor();
        } else {
            CompoundNBT nbt = itemStack.getOrCreateTag();
            return nbt.contains(NBT_TAG_COLOR) ? DyeColor.byId(nbt.getInt(NBT_TAG_COLOR)): DyeColor.WHITE;
        }
    }

    public static DyeColor getColor(BlockState blockState) {
        if (blockState.getBlock() instanceof IColored) {
            return ((IColored) blockState.getBlock()).getColor();
        }
        return DyeColor.WHITE;
    }

    public static void setColor(ItemStack itemStack, DyeColor color) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.putInt(NBT_TAG_COLOR, color.getId());
    }

    public static boolean hasColor(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        return nbt.contains(NBT_TAG_COLOR);
    }

    public static void removeColor(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.remove(NBT_TAG_COLOR);
    }

    public static String removeColorSuffix(String str) {
        for (DyeColor color: DyeColor.values()) {
            str = str.replaceAll("(_light)?_" + color.getName(), "");
        }
        return str;
    }

    public static String coloredColorName(DyeColor color) {
        String colorName = color.getName().replace("_", " ");
        colorName = Arrays.stream(colorName.split(" ")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        return DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + colorName;
    }

    public static ITextComponent getDisplayItemColorName(ItemStack itemStack, String itemName) {
        DyeColor color = ColorUtils.getColor(itemStack);
        String colorName = color.getName().replace("_", " ");
        colorName = Arrays.stream(colorName.split(" ")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        return new StringTextComponent(ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + colorName + " " + itemName);
    }

    public interface IColored {

        DyeColor getColor();

    }
}
