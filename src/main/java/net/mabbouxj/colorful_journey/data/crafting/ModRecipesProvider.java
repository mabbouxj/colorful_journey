package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        woodRecipes(consumer);

        ShapedRecipeBuilder.shaped(ModItems.INK_BALL.get(), 4)
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .define('#', Tags.Items.DYES)
                .unlockedBy("has_" + ModItems.INK_BALL.getId().getPath(), has(ModItems.INK_BALL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.COLOR_GUN.get(), 1)
                .pattern(" o ")
                .pattern("###")
                .pattern("  i")
                .define('o', Items.GLASS_BOTTLE)
                .define('#', Items.IRON_INGOT)
                .define('i', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ModItems.COLOR_GUN.getId().getPath(), has(ModItems.COLOR_GUN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.COLOR_LASER_GUN.get(), 1)
                .pattern(" o ")
                .pattern("###")
                .pattern("  i")
                .define('o', Items.GLASS_BOTTLE)
                .define('#', Items.NETHERITE_INGOT)
                .define('i', Tags.Items.RODS_BLAZE)
                .unlockedBy("has_" + ModItems.COLOR_LASER_GUN.getId().getPath(), has(ModItems.COLOR_LASER_GUN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.RUBIKS_CUBE_UNFINISHED.get(), 1)
                .pattern("wr#")
                .pattern("g#b")
                .pattern("#oy")
                .define('#', Tags.Items.NUGGETS_IRON)
                .define('w', Tags.Items.DYES_WHITE)
                .define('r', Tags.Items.DYES_RED)
                .define('g', Tags.Items.DYES_GREEN)
                .define('b', Tags.Items.DYES_BLUE)
                .define('o', Tags.Items.DYES_ORANGE)
                .define('y', Tags.Items.DYES_YELLOW)
                .unlockedBy("has_" + ModItems.RUBIKS_CUBE_UNFINISHED.getId().getPath(), has(ModItems.RUBIKS_CUBE_UNFINISHED.get()))
                .save(consumer);

    }

    private void woodRecipes(Consumer<IFinishedRecipe> consumer) {
        for (DyeColor color: ColorfulJourney.COLORS) {
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


    protected final ResourceLocation locWood(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "wood/" + name);
    }

}
