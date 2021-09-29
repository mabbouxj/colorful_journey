package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
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
            String name = "colored_" + color.getName();
            planksBlock(consumer, name + "_from_logs", ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_LOGS.get(color));
            planksBlock(consumer, name + "_from_stripped_logs", ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_STRIPPED_LOGS.get(color));
            planksBlock(consumer, name + "_from_wood", ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_WOODS.get(color));
            planksBlock(consumer, name + "_from_stripped_wood", ModBlocks.COLORED_PLANKS.get(color), ModBlocks.COLORED_STRIPPED_WOODS.get(color));

            woodBlock(consumer, name, ModBlocks.COLORED_WOODS.get(color), ModBlocks.COLORED_LOGS.get(color));
            strippedWoodBlock(consumer, name, ModBlocks.COLORED_STRIPPED_WOODS.get(color), ModBlocks.COLORED_STRIPPED_LOGS.get(color));
        }
    }

    protected final void planksBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapelessRecipeBuilder.shapeless(result.get(), 4)
                .requires(material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood(name + "_planks"));
    }

    protected final void woodBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood(name + "_wood"));
    }

    protected final void strippedWoodBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locWood(name + "_stripped_wood"));
    }

}
