package net.mabbouxj.colorful_journey.world.gen;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.item.DyeColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;


public class TreeGeneration {

    public static void generateTrees(final BiomeLoadingEvent event) {
        if (!(event.getCategory().equals(Biome.Category.NETHER) || event.getCategory().equals(Biome.Category.THEEND))) {
            for (DyeColor color: ColorfulJourney.ENABLED_COLORS) {
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
                        ModConfiguredFeatures.WILD_BASE_COLORED_TREES.get(color));
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
                        ModConfiguredFeatures.WILD_FANCY_COLORED_TREES.get(color));
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
                        ModConfiguredFeatures.WILD_MEGA_COLORED_TREES.get(color));
            }
        }
    }

}
