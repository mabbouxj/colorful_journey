package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.data.tag.ModItemTagProvider;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColoredVariantsItem;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        woodRecipes(consumer);
        metalRecipes(consumer);
        washingMachineRecipes(consumer);

        ShapedRecipeBuilder.shaped(ModItems.PAINTBRUSH.get(), 1)
                .pattern("  h")
                .pattern(" o ")
                .pattern("i  ")
                .define('h', ModItems.HORSEHAIR.get())
                .define('o', Tags.Items.INGOTS_IRON)
                .define('i', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ModItems.PAINTBRUSH.getId().getPath(), has(ModItems.PAINTBRUSH.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.COLOR_PALETTE.get(), 1)
                .pattern("#o#")
                .pattern("o#o")
                .pattern("#o#")
                .define('#', ItemTags.PLANKS)
                .define('o', Tags.Items.DYES)
                .unlockedBy("has_" + ModItems.COLOR_PALETTE.getId().getPath(), has(ModItems.COLOR_PALETTE.get()))
                .save(consumer);

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
                .define('w', ModItems.DENSE_DYES.get(DyeColor.WHITE).get())
                .define('r', ModItems.DENSE_DYES.get(DyeColor.RED).get())
                .define('g', ModItems.DENSE_DYES.get(DyeColor.GREEN).get())
                .define('b', ModItems.DENSE_DYES.get(DyeColor.BLUE).get())
                .define('o', ModItems.DENSE_DYES.get(DyeColor.ORANGE).get())
                .define('y', ModItems.DENSE_DYES.get(DyeColor.YELLOW).get())
                .unlockedBy("has_" + ModItems.RUBIKS_CUBE_UNFINISHED.getId().getPath(), has(ModItems.RUBIKS_CUBE_UNFINISHED.get()))
                .save(consumer);

        resetNBTRecipe(consumer, ModItems.ENERGY_DYE_GENERATOR.get());
        ShapedRecipeBuilder.shaped(ModItems.ENERGY_DYE_GENERATOR.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('#', ModItemTagProvider.COLORED_INGOTS)
                .define('x', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.NETHERITE_INGOT)
                .unlockedBy("has_" + ModItems.ENERGY_DYE_GENERATOR.getId().getPath(), has(ModItems.ENERGY_DYE_GENERATOR.get()))
                .save(consumer);

        resetNBTRecipe(consumer, ModItems.ENERGY_CAPACITOR.get());
        ShapedRecipeBuilder.shaped(ModItems.ENERGY_CAPACITOR.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('x', ModItemTagProvider.COLORED_INGOTS)
                .define('#', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.NETHERITE_INGOT)
                .unlockedBy("has_" + ModItems.ENERGY_CAPACITOR.getId().getPath(), has(ModItems.ENERGY_CAPACITOR.get()))
                .save(consumer);

        resetNBTRecipe(consumer, ModItems.WASHING_MACHINE.get());
        ShapedRecipeBuilder.shaped(ModItems.WASHING_MACHINE.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('x', ModItemTagProvider.COLORED_INGOTS)
                .define('#', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.WATER_BUCKET)
                .unlockedBy("has_" + ModItems.WASHING_MACHINE.getId().getPath(), has(ModItems.WASHING_MACHINE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.COLORED_PASSIVE_AGGLOMERA.get(), 1)
                .requires(ModItemTagProvider.COLORED_FEATHERS)
                .requires(ModItemTagProvider.COLORED_BAMBOOS)
                .requires(ModItemTagProvider.COLORED_LEATHERS)
                .requires(ModItemTagProvider.COLORED_EGGS)
                .requires(ModItemTagProvider.COLORED_HONEYCOMBS)
                .requires(ModItemTagProvider.COLORED_BAT_WINGS)
                .requires(ModItemTagProvider.COLORED_TENTACLES)
                .unlockedBy("has_item", has(ModItems.COLORED_PASSIVE_AGGLOMERA.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.COLORED_AGGRESSIVE_AGGLOMERA.get(), 1)
                .requires(ModItemTagProvider.COLORED_BONES)
                .requires(ModItemTagProvider.COLORED_ENDER_PEARLS)
                .requires(ModItemTagProvider.COLORED_BLAZE_RODS)
                .requires(ModItemTagProvider.COLORED_ROTTEN_FLESHES)
                .requires(ModItemTagProvider.COLORED_GHAST_TEARS)
                .requires(ModItemTagProvider.COLORED_PHANTOM_MEMBRANES)
                .requires(ModItemTagProvider.COLORED_STRINGS)
                .requires(ModItemTagProvider.COLORED_SKULLS)
                .requires(ModItemTagProvider.COLORED_GUNPOWDERS)
                .unlockedBy("has_item", has(ModItems.COLORED_AGGRESSIVE_AGGLOMERA.get()))
                .save(consumer);

        for (DyeColor color: DyeColor.values()) {
            ShapedRecipeBuilder.shaped(ModItems.DENSE_DYES.get(color).get(), 1)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .define('#', ColorUtils.getDyeItemByColor(color))
                    .unlockedBy("has_item", has(ModItems.DENSE_DYES.get(color).get()))
                    .save(consumer);
            ShapedRecipeBuilder.shaped(ModItems.COLORED_PORTAL_FRAMES.get(color).get(), 1)
                    .pattern("#o#")
                    .pattern("oxo")
                    .pattern("#o#")
                    .define('#', Items.NETHERITE_INGOT)
                    .define('o', ModItems.DENSE_DYES.get(color).get())
                    .define('x', ModItems.COLORED_NETHER_STARS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORED_PORTAL_FRAMES.get(color).get()))
                    .save(consumer);
        }

    }

    private void resetNBTRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider itemProvider) {
        ShapelessRecipeBuilder.shapeless(itemProvider, 1)
                .requires(itemProvider)
                .unlockedBy("has_item", has(itemProvider))
                .save(consumer, itemProvider.asItem().getRegistryName().getPath() + "_reset");
    }

    private void washingMachineRecipes(Consumer<IFinishedRecipe> consumer) {
        for (RegistryObject<Item> registryItem: ModItems.ALL_COLORED_VARIANTS_ITEMS) {
            if (registryItem.get() instanceof ColoredVariantsItem) {
                ColoredVariantsItem coloredVariantsItem = (ColoredVariantsItem) registryItem.get();
                if (coloredVariantsItem.getInitialItem() != null) {
                    new WashingMachineRecipe.Builder(registryItem.get(), 1)
                            .withOutput(coloredVariantsItem.getInitialItem(), 1)
                            .withOutputAlt(ColorUtils.getDyeItemByColor(coloredVariantsItem.getColor()), 1)
                            .save(consumer);
                }
            }
        }
    }

    private void metalRecipes(Consumer<IFinishedRecipe> consumer) {
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

    private void woodRecipes(Consumer<IFinishedRecipe> consumer) {
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


    protected final ResourceLocation locWood(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "wood/" + name);
    }

    protected  final ResourceLocation locMetal(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "metal/" + name);
    }

}
