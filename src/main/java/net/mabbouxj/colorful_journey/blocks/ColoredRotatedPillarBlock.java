package net.mabbouxj.colorful_journey.blocks;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.DyeColor;


public class ColoredRotatedPillarBlock extends RotatedPillarBlock implements IColoredBlock {

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
