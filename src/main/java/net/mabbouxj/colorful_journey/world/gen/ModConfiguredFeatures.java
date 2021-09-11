package net.mabbouxj.colorful_journey.world.gen;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.item.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.MegaPineFoliagePlacer;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.GiantTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;

public class ModConfiguredFeatures {

    public static final Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> COLORED_TREE = registerColoredTreeFeature("colored_tree", ModConfiguredFeatures::baseColoredTreeFeatures);
    public static final Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> FANCY_COLORED_TREE = registerColoredTreeFeature("fancy_colored_tree", ModConfiguredFeatures::fancyColoredTreeFeatures);
    public static final Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> MEGA_COLORED_TREE = registerColoredTreeFeature("mega_colored_tree", ModConfiguredFeatures::megaColoredTreeFeatures);
    public static final Map<DyeColor, ConfiguredFeature<?, ?>> PATCH_COLORED_GRASS = registerColoredFeature("patch_colored_grass", Feature.RANDOM_PATCH.configured(Features.Configs.DEFAULT_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(-0.8D, 5, 10))));

    private static Map<DyeColor, ConfiguredFeature<?, ?>> registerColoredFeature(String name, ConfiguredFeature<?, ?> feature) {
        Map<DyeColor, ConfiguredFeature<?, ?>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            ConfiguredFeature<?, ?> fc = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name + "_" + color.getName(), feature);
            map.put(color, fc);
        }
        return map;
    }

    private static ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> baseColoredTreeFeatures (DyeColor color) {
        return Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                new StraightTrunkPlacer(4, 2, 0),
                new TwoLayerFeature(1, 0, 1))).ignoreVines().build());
    }

    private static ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> fancyColoredTreeFeatures (DyeColor color) {
        return Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
                new FancyTrunkPlacer(3, 11, 0),
                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build());
    }

    private static ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> megaColoredTreeFeatures (DyeColor color) {
        return Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.COLORED_LOGS.get(color).get().defaultBlockState()),
                new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get(color).get().defaultBlockState()),
                new MegaPineFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0), FeatureSpread.of(13, 4)),
                new GiantTrunkPlacer(13, 2, 14),
                new TwoLayerFeature(1, 1, 2))).build());
    }

    private static Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ?>> registerColoredTreeFeature(String name, Function<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> supplier) {
        Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ?>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            map.put(color, Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name + "_" + color.getName(), supplier.apply(color)));
        }
        return map;
    }

}
