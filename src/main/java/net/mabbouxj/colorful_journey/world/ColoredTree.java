package net.mabbouxj.colorful_journey.world;

import net.mabbouxj.colorful_journey.world.gen.ModConfiguredFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.item.DyeColor;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class ColoredTree extends BigTree {

    private final DyeColor color;

    public ColoredTree(DyeColor color) {
        this.color = color;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean largeHive) {
        if (random.nextInt(10) == 0) {
            return ModConfiguredFeatures.FANCY_COLORED_TREE.get(color);
        } else {
            return ModConfiguredFeatures.COLORED_TREE.get(color);
        }
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredMegaFeature(Random random) {
        return ModConfiguredFeatures.MEGA_COLORED_TREE.get(color);
    }
}
