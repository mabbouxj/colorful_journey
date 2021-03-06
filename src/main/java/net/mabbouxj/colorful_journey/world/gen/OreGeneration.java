package net.mabbouxj.colorful_journey.world.gen;

import com.mojang.datafixers.util.Pair;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.HashMap;
import java.util.Map;

public class OreGeneration {

    public static final RuleTest END_STONE_RULE_TEST = new BlockMatchRuleTest(Blocks.END_STONE);
    public static Map<DyeColor, Pair<Integer, Integer>> OVERWORLD_COLORED_ORE_GEN_MIN_MAX = new HashMap<>();

    static {
        int nbColors = ColorfulJourney.ENABLED_COLORS.size();
        int allMinHeight = 5;
        int allMaxHeight = 50;
        int oreVeinHeight = 10;
        float ratio = (allMaxHeight - allMinHeight - oreVeinHeight) / (float) nbColors;
        int i = 0;

        for (DyeColor color: ColorfulJourney.ENABLED_COLORS) {
            int minHeight = (int) (i++ * ratio) + allMinHeight;
            int maxHeight = minHeight + oreVeinHeight;
            OVERWORLD_COLORED_ORE_GEN_MIN_MAX.put(color, Pair.of(minHeight, maxHeight));
        }
    }

    public static void generateOres(final BiomeLoadingEvent event) {
        if (!(event.getCategory().equals(Biome.Category.NETHER) || event.getCategory().equals(Biome.Category.THEEND))) {
            for (DyeColor color: ColorfulJourney.ENABLED_COLORS) {
                generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        ModBlocks.COLORED_ORES.get(color).get().defaultBlockState(), 10,
                        OVERWORLD_COLORED_ORE_GEN_MIN_MAX.get(color).getFirst(), OVERWORLD_COLORED_ORE_GEN_MIN_MAX.get(color).getSecond(), 10);
            }
        }

        if (event.getCategory().equals(Biome.Category.NETHER)) {
            for (DyeColor color: ColorfulJourney.ENABLED_COLORS) {
                generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NETHERRACK,
                        ModBlocks.COLORED_NETHER_ORES.get(color).get().defaultBlockState(), 8,
                        0, 256, 10);
            }
        }

        if (event.getCategory().equals(Biome.Category.THEEND)) {
            for (DyeColor color: ColorfulJourney.ENABLED_COLORS) {
                generateOre(event.getGeneration(), END_STONE_RULE_TEST,
                        ModBlocks.COLORED_END_ORES.get(color).get().defaultBlockState(), 6,
                        0, 256, 10);
            }
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state,
                                    int veinSize, int minHeight, int maxHeight, int amount) {
        settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreFeatureConfig(fillerType, state, veinSize))
                        .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .squared().count(amount));
    }

}
