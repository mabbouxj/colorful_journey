package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.DyeColor;


public class ColoredRotatedPillarBlock extends RotatedPillarBlock implements ColorUtils.IColored {

    private final DyeColor color;
    
    public ColoredRotatedPillarBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
