package net.mabbouxj.colorful_journey.world.gen;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.world.ColoredTree;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModConfiguredFeatures {

    //public static final Map<DyeColor, ConfiguredFeature<?, ?>> PATCH_COLORED_GRASS = registerColoredFeature("patch_colored_grass", Feature.RANDOM_PATCH.configured(Features.Configs.DEFAULT_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(-0.8D, 5, 10))));
    public static final Map<DyeColor, ConfiguredFeature<?, ?>> WILD_BASE_COLORED_TREES = registerColoredTreeFeature("wild_base_colored_tree", ColoredTree::baseColoredTreeFeatures, ColoredTree.WILD_BASE_TREE_SPAWN_CHANCE);
    public static final Map<DyeColor, ConfiguredFeature<?, ?>> WILD_FANCY_COLORED_TREES = registerColoredTreeFeature("wild_fancy_colored_tree", ColoredTree::fancyColoredTreeFeatures, ColoredTree.WILD_FANCY_TREE_SPAWN_CHANCE);
    public static final Map<DyeColor, ConfiguredFeature<?, ?>> WILD_MEGA_COLORED_TREES = registerColoredTreeFeature("wild_mega_colored_tree", ColoredTree::megaColoredTreeFeatures, ColoredTree.WILD_MEGA_TREE_SPAWN_CHANCE);

    private static Map<DyeColor, ConfiguredFeature<?, ?>> registerColoredTreeFeature(String name, Function<DyeColor, BaseTreeFeatureConfig> supplier, float spawnChance) {
        Map<DyeColor, ConfiguredFeature<?, ?>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            map.put(color, Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                    new ResourceLocation(ColorfulJourney.MOD_ID, name + "_" + color.getName()),
                    Feature.TREE.configured(supplier.apply(color))
                            .decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(0, spawnChance, 1)))));
        }
        return map;
    }

    private static Map<DyeColor, ConfiguredFeature<?, ?>> registerColoredFeature(String name, ConfiguredFeature<?, ?> feature) {
        Map<DyeColor, ConfiguredFeature<?, ?>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            ConfiguredFeature<?, ?> fc = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name + "_" + color.getName(), feature);
            map.put(color, fc);
        }
        return map;
    }

}
