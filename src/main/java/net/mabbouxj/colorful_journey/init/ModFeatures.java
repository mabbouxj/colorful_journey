package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ColorfulJourney.MOD_ID);

    /*public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> COLORED_TREE = Registry.register(
            WorldGenRegistries.CONFIGURED_FEATURE,
            "colored_tree",
            Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(ModBlocks.COLORED_LOG.get().defaultBlockState()),
            new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get().defaultBlockState()),
            new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1))).ignoreVines().build()));


    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FANCY_COLORED_TREE = Registry.register(
            WorldGenRegistries.CONFIGURED_FEATURE,
            "fancy_colored_tree",
            Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(ModBlocks.COLORED_LOG.get().defaultBlockState()),
                    new SimpleBlockStateProvider(ModBlocks.COLORED_LEAVES.get().defaultBlockState()),
                    new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
                    new FancyTrunkPlacer(3, 11, 0),
                    new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()));
*/

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }

}
