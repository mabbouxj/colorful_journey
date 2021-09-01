package net.mabbouxj.colorful_journey.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;


public class ColoredLogBlock extends RotatedPillarBlock implements IColoredBlock {

    private final DyeColor color;
    
    public ColoredLogBlock(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
