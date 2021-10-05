package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.data.tag.ModItemTagProvider;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WoodRecipes extends RecipeProvider {

    public WoodRecipes(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    protected final ResourceLocation locWood(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "wood/" + name);
    }

    public void generate(Consumer<IFinishedRecipe> consumer) {
        for (DyeColor color: DyeColor.values()) {
            String colorName = color.getName();
            planksBlock(consumer, "from_logs_" + colorName, ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_LOGS.get(color));
            planksBlock(consumer, "from_stripped_logs" + colorName, ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_STRIPPED_LOGS.get(color));
            planksBlock(consumer, "from_wood" + colorName, ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_WOODS.get(color));
            planksBlock(consumer, "from_stripped_wood" + colorName, ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_STRIPPED_WOODS.get(color));

            woodBlock(consumer, colorName, ModBlocks.COLORED_WOODS.get(color), ModBlocks.COLORED_LOGS.get(color));
            strippedWoodBlock(consumer, colorName, ModBlocks.COLORED_STRIPPED_WOODS.get(color), ModBlocks.COLORED_STRIPPED_LOGS.get(color));
        }

        ShapedRecipeBuilder.shaped(Items.CHEST, 4)
                .pattern("###").pattern("# #").pattern("###")
                .define('#', ItemTags.LOGS)
                .unlockedBy("has_item", has(ModItemTagProvider.COLORED_LOGS))
                .save(consumer, locWood("chests_from_logs"));

        ShapedRecipeBuilder.shaped(Items.STICK, 16)
                .pattern("#").pattern("#")
                .define('#', ItemTags.LOGS)
                .unlockedBy("has_item", has(ModItemTagProvider.COLORED_LOGS))
                .save(consumer, locWood("sticks_from_logs"));
    }

    protected final void planksBlock(Consumer<IFinishedRecipe> consumer, String suffix, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapelessRecipeBuilder.shapeless(result.get(), 4)
                .requires(material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood("planks_" + suffix));
    }

    protected final void woodBlock(Consumer<IFinishedRecipe> consumer, String suffix, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood("wood_" + suffix));
    }

    protected final void strippedWoodBlock(Consumer<IFinishedRecipe> consumer, String suffix, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood("stripped_wood_" + suffix));
    }

}
