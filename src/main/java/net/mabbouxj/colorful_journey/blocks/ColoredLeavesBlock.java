package net.mabbouxj.colorful_journey.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;


public class ColoredLeavesBlock extends LeavesBlock implements IColoredBlock {

    private final DyeColor color;

    public ColoredLeavesBlock(DyeColor color) {
        super(AbstractBlock.Properties
                .of(Material.LEAVES)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn(ColoredLeavesBlock::ocelotOrParrot)
                .isSuffocating(ColoredLeavesBlock::never)
                .isViewBlocking(ColoredLeavesBlock::never));
        this.color = color;
    }

    private static Boolean ocelotOrParrot(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }

    private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
