package net.mabbouxj.colorful_journey.world;

import net.mabbouxj.colorful_journey.init.ModFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.DyeColor;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class ColoredTree extends Tree {

    private DyeColor color;

    public ColoredTree(DyeColor color) {
        this.color = color;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean largeHive) {
        if (random.nextInt(10) == 0) {
            return ModFeatures.FANCY_COLORED_TREE.get(color);
        } else {
            return ModFeatures.COLORED_TREE.get(color);
        }
    }
}
