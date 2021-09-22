package net.mabbouxj.colorful_journey.world;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.trees.BigTree;
import net.minecraft.item.DyeColor;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.MegaPineFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.GiantTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;

public class ColoredTree extends BigTree {

    public static float WILD_BASE_TREE_SPAWN_CHANCE = 0.07f;
    public static float WILD_FANCY_TREE_SPAWN_CHANCE = 0.05f;
    public static float WILD_MEGA_TREE_SPAWN_CHANCE = 0.03f;
    private final DyeColor color;

    public ColoredTree(DyeColor color) {
        this.color = color;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean largeHive) {
        if (random.nextInt(10) == 0) {
            return Feature.TREE.configured(fancyColoredTreeFeatures(color));
        } else {
            return Feature.TREE.configured(baseColoredTreeFeatures(color));
        }
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredMegaFeature(Random random) {
        return Feature.TREE.configured(megaColoredTreeFeatures(color));
    }

    public static BaseTreeFeatureConfig baseColoredTreeFeatures (DyeColor color) {
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                new StraightTrunkPlacer(4, 2, 0),
                new TwoLayerFeature(1, 0, 1)).ignoreVines().build();
    }

    public static BaseTreeFeatureConfig fancyColoredTreeFeatures (DyeColor color) {
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
                new FancyTrunkPlacer(3, 11, 0),
                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))).ignoreVines().build();
    }

    public static BaseTreeFeatureConfig megaColoredTreeFeatures (DyeColor color) {
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new MegaPineFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0), FeatureSpread.of(13, 4)),
                new GiantTrunkPlacer(13, 2, 14),
                new TwoLayerFeature(1, 1, 2)).build();
    }

}
