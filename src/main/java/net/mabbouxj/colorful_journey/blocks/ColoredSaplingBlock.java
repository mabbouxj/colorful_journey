package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.world.ColoredTree;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class ColoredSaplingBlock extends SaplingBlock implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredSaplingBlock(DyeColor color) {
        super(new ColoredTree(color), AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
