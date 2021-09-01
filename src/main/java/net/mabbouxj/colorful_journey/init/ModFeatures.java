package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.item.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;

public class ModFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ColorfulJourney.MOD_ID);

    public static final Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> COLORED_TREE = registerColoredTreeFeature("colored_tree", ModFeatures::baseColoredTreeFeatures);
    public static final Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> FANCY_COLORED_TREE = registerColoredTreeFeature("fancy_colored_tree", ModFeatures::fancyColoredTreeFeatures);

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

    private static Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ?>> registerColoredTreeFeature(String name, Function<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>>> supplier) {
        Map<DyeColor, ConfiguredFeature<BaseTreeFeatureConfig, ?>> map = new HashMap<>();
        for (DyeColor color: ColorfulJourney.COLORS) {
            map.put(color, Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name + "_" + color.getName(), supplier.apply(color)));
        }
        return map;
    }

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }

}
