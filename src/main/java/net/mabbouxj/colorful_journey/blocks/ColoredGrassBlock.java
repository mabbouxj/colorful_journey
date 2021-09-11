package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class ColoredGrassBlock extends GrassBlock implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredGrassBlock(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS));
        this.color = color;
    }

    @Override
    public void performBonemeal(ServerWorld world, Random random, BlockPos blockPos, BlockState blockState) {
        BlockPos blockpos = blockPos.above();
        BlockState blockstate = ModBlocks.COLORED_GRASS_BLOCKS.get(this.color).get().defaultBlockState();

        anchor:
        for(int i = 0; i < 128; ++i) {
            BlockPos blockpos1 = blockpos;

            for(int j = 0; j < i / 16; ++j) {
                blockpos1 = blockpos1.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockpos1.below()).is(this) || world.getBlockState(blockpos1).isCollisionShapeFullBlock(world, blockpos1)) {
                    continue anchor;
                }
            }

            BlockState blockstate2 = world.getBlockState(blockpos1);
            if (blockstate2.is(blockstate.getBlock()) && random.nextInt(10) == 0) {
                ((IGrowable)blockstate.getBlock()).performBonemeal(world, random, blockpos1, blockstate2);
            }

            if (blockstate2.isAir()) {
                BlockState blockstate1;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) {
                        continue;
                    }

                    ConfiguredFeature<?, ?> configuredfeature = list.get(0);
                    FlowersFeature flowersfeature = (FlowersFeature)configuredfeature.feature;
                    blockstate1 = flowersfeature.getRandomFlower(random, blockpos1, configuredfeature.config());
                } else {
                    blockstate1 = blockstate;
                }

                if (blockstate1.canSurvive(world, blockpos1)) {
                    world.setBlock(blockpos1, blockstate1, 3);
                }
            }
        }
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

}
