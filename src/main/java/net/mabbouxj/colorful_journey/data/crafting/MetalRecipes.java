package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class MetalRecipes extends RecipeProvider {

    public MetalRecipes(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    protected  final ResourceLocation locMetal(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "metal/" + name);
    }

    public void generate(Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapeless(ModItems.UNCOLORED_NUGGET.get(), 9)
                .requires(ModItems.UNCOLORED_INGOT.get())
                .unlockedBy("has_item", has(ModItems.UNCOLORED_INGOT.get()))
                .save(consumer, locMetal("uncolored_nuggets_from_ingot"));

        ShapelessRecipeBuilder.shapeless(ModItems.UNCOLORED_INGOT.get(), 9)
                .requires(ModItems.UNCOLORED_INGOT_BLOCK.get())
                .unlockedBy("has_item", has(ModItems.UNCOLORED_INGOT_BLOCK.get()))
                .save(consumer, locMetal("uncolored_ingots_from_block"));

        ShapedRecipeBuilder.shaped(ModItems.UNCOLORED_INGOT.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.UNCOLORED_NUGGET.get())
                .unlockedBy("has_item", has(ModItems.UNCOLORED_NUGGET.get()))
                .save(consumer, locMetal("uncolored_ingot_from_nuggets"));

        ShapedRecipeBuilder.shaped(ModItems.UNCOLORED_INGOT_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.UNCOLORED_INGOT.get())
                .unlockedBy("has_item", has(ModItems.UNCOLORED_INGOT.get()))
                .save(consumer, locMetal("uncolored_block_from_ingots"));

        for (DyeColor color: DyeColor.values()) {
            String name = "colored_" + color.getName();

            CookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.COLORED_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 0.7f, 200)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_smelting"));

            CookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.COLORED_NETHER_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 1.0f, 180)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_NETHER_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_smelting_nether"));

            CookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.COLORED_END_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 1.5f, 160)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_END_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_smelting_end"));

            CookingRecipeBuilder.blasting(Ingredient.of(ModBlocks.COLORED_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 0.7f, 100)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_blasting"));

            CookingRecipeBuilder.blasting(Ingredient.of(ModBlocks.COLORED_NETHER_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 1.0f, 90)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_NETHER_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_blasting_nether"));

            CookingRecipeBuilder.blasting(Ingredient.of(ModBlocks.COLORED_END_ORES.get(color).get()), ModItems.COLORED_INGOTS.get(color).get(), 1.5f, 80)
                    .unlockedBy("has_item", has(ModBlocks.COLORED_END_ORES.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_blasting_end"));

            ShapelessRecipeBuilder.shapeless(ModItems.COLORED_NUGGETS.get(color).get(), 9)
                    .requires(ModItems.COLORED_INGOTS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORED_INGOTS.get(color).get()))
                    .save(consumer, locMetal(name + "_nuggets"));

            ShapelessRecipeBuilder.shapeless(ModItems.COLORED_INGOTS.get(color).get(), 9)
                    .requires(ModItems.COLORED_INGOT_BLOCKS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORED_INGOT_BLOCKS.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_block"));

            ShapedRecipeBuilder.shaped(ModItems.COLORED_INGOTS.get(color).get(), 1)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .define('#', ModItems.COLORED_NUGGETS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORED_NUGGETS.get(color).get()))
                    .save(consumer, locMetal(name + "_ingots_from_nuggets"));

            ShapedRecipeBuilder.shaped(ModItems.COLORED_INGOT_BLOCKS.get(color).get(), 1)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .define('#', ModItems.COLORED_INGOTS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORED_INGOTS.get(color).get()))
                    .save(consumer, locMetal(name + "_block"));
        }
    }

}
