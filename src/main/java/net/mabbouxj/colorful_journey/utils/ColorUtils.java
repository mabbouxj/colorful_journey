package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;

import java.util.Arrays;
import java.util.Random;

public class ColorUtils {

    public static final String NBT_TAG_NAME_COLOR = "color";
    public static final IntegerProperty STATE_COLOR_ID = IntegerProperty.create("color", 0, 15);

    public static DyeColor getRandomDyeColor() {
        return Arrays.asList(ColorfulJourney.COLORS).get(new Random().nextInt(ColorfulJourney.COLORS.length));
    }

    public static DyeColor getColor(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains(NBT_TAG_NAME_COLOR)) {
            return DyeColor.byId(nbt.getInt(NBT_TAG_NAME_COLOR));
        }
        return DyeColor.WHITE;
    }

    public static int getColorId(BlockState state) {
        return state.getValue(STATE_COLOR_ID);
    }

    public static void setColor(ItemStack stack, DyeColor color) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt(NBT_TAG_NAME_COLOR, color.getId());
    }

}
