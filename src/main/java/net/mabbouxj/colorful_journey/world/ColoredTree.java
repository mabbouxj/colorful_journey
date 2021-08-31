package net.mabbouxj.colorful_journey.world;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class ColoredTree extends Tree {
    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean withBees) {
        /*if (random.nextInt(10) == 0) {
            return withBees ? ModFeatures.FANCY_COLORED_TREE : ModFeatures.FANCY_COLORED_TREE;
        } else {
            return withBees ? ModFeatures.COLORED_TREE : ModFeatures.COLORED_TREE;
        }*/return null;
    }
}
