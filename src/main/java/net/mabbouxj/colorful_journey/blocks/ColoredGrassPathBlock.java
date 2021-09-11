package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.GrassPathBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class ColoredGrassPathBlock extends GrassPathBlock implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredGrassPathBlock(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.DIRT).strength(0.65F).sound(SoundType.GRASS).isViewBlocking((x, y, z) -> true).isSuffocating((x, y, z) -> true));
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
