package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;

public class ColoredBlock extends Block implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
