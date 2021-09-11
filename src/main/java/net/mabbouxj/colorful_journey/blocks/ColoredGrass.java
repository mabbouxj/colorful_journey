package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class ColoredGrass extends TallGrassBlock implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredGrass(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS));
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

}
