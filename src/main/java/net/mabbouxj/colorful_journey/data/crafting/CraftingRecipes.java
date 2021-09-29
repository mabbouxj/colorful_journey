package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.data.tag.ModItemTagProvider;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CraftingRecipes extends RecipeProvider {

    public CraftingRecipes(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    private ResourceLocation locCrafting(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "crafting/" + name);
    }

    public void generate(Consumer<IFinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(ModItems.PAINTBRUSH.get(), 1)
                .pattern("  h")
                .pattern(" o ")
                .pattern("i  ")
                .define('h', ModItems.HORSEHAIR.get())
                .define('o', Tags.Items.INGOTS_IRON)
                .define('i', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ModItems.PAINTBRUSH.getId().getPath(), has(ModItems.PAINTBRUSH.get()))
                .save(consumer, locCrafting("paintbrush"));

        ShapedRecipeBuilder.shaped(ModItems.COLOR_PALETTE.get(), 1)
                .pattern("#o#")
                .pattern("o#o")
                .pattern("#o#")
                .define('#', ItemTags.PLANKS)
                .define('o', Tags.Items.DYES)
                .unlockedBy("has_" + ModItems.COLOR_PALETTE.getId().getPath(), has(ModItems.COLOR_PALETTE.get()))
                .save(consumer, locCrafting("color_palette"));

        ShapedRecipeBuilder.shaped(ModItems.INK_BALL.get(), 4)
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .define('#', Tags.Items.DYES)
                .unlockedBy("has_" + ModItems.INK_BALL.getId().getPath(), has(ModItems.INK_BALL.get()))
                .save(consumer, locCrafting("ink_ball"));

        ShapedRecipeBuilder.shaped(ModItems.COLOR_GUN.get(), 1)
                .pattern(" o ")
                .pattern("###")
                .pattern("  i")
                .define('o', Items.GLASS_BOTTLE)
                .define('#', Items.IRON_INGOT)
                .define('i', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ModItems.COLOR_GUN.getId().getPath(), has(ModItems.COLOR_GUN.get()))
                .save(consumer, locCrafting("color_gun"));

        ShapedRecipeBuilder.shaped(ModItems.COLOR_LASER_GUN.get(), 1)
                .pattern(" o ")
                .pattern("###")
                .pattern("  i")
                .define('o', Items.GLASS_BOTTLE)
                .define('#', Items.NETHERITE_INGOT)
                .define('i', Tags.Items.RODS_BLAZE)
                .unlockedBy("has_" + ModItems.COLOR_LASER_GUN.getId().getPath(), has(ModItems.COLOR_LASER_GUN.get()))
                .save(consumer, locCrafting("color_laser_gun"));

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
                .save(consumer, locCrafting("rubiks_cube_unfinished"));

        resetNBTRecipe(consumer, ModItems.ENERGY_DYE_GENERATOR.get());
        ShapedRecipeBuilder.shaped(ModItems.ENERGY_DYE_GENERATOR.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('#', ModItemTagProvider.COLORED_INGOTS)
                .define('x', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.NETHERITE_INGOT)
                .unlockedBy("has_" + ModItems.ENERGY_DYE_GENERATOR.getId().getPath(), has(ModItems.ENERGY_DYE_GENERATOR.get()))
                .save(consumer, locCrafting("energy_dye_generator"));

        resetNBTRecipe(consumer, ModItems.ENERGY_CAPACITOR.get());
        ShapedRecipeBuilder.shaped(ModItems.ENERGY_CAPACITOR.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('x', ModItemTagProvider.COLORED_INGOTS)
                .define('#', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.NETHERITE_INGOT)
                .unlockedBy("has_" + ModItems.ENERGY_CAPACITOR.getId().getPath(), has(ModItems.ENERGY_CAPACITOR.get()))
                .save(consumer, locCrafting("energy_capacitor"));

        resetNBTRecipe(consumer, ModItems.WASHING_MACHINE.get());
        ShapedRecipeBuilder.shaped(ModItems.WASHING_MACHINE.get(), 1)
                .pattern("#x#")
                .pattern("xox")
                .pattern("#x#")
                .define('x', ModItemTagProvider.COLORED_INGOTS)
                .define('#', ModItems.RUBIKS_CUBE.get())
                .define('o', Items.WATER_BUCKET)
                .unlockedBy("has_" + ModItems.WASHING_MACHINE.getId().getPath(), has(ModItems.WASHING_MACHINE.get()))
                .save(consumer, locCrafting("washing_machine"));

        ShapelessRecipeBuilder.shapeless(ModItems.COLORED_PASSIVE_AGGLOMERA.get(), 1)
                .requires(ModItemTagProvider.COLORED_FEATHERS)
                .requires(ModItemTagProvider.COLORED_BAMBOOS)
                .requires(ModItemTagProvider.COLORED_LEATHERS)
                .requires(ModItemTagProvider.COLORED_EGGS)
                .requires(ModItemTagProvider.COLORED_HONEYCOMBS)
                .requires(ModItemTagProvider.COLORED_BAT_WINGS)
                .requires(ModItemTagProvider.COLORED_TENTACLES)
                .unlockedBy("has_item", has(ModItems.COLORED_PASSIVE_AGGLOMERA.get()))
                .save(consumer, locCrafting("colored_passive_agglomera"));

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
                .save(consumer, locCrafting("colored_aggressive_agglomera"));

        ShapedRecipeBuilder.shaped(ModItems.COLORFUL_PORTAL_FRAME.get(), 1)
                .pattern("#o#")
                .pattern("oxo")
                .pattern("#o#")
                .define('#', Items.IRON_INGOT)
                .define('o', ModItemTagProvider.COLORED_INGOTS)
                .define('x', Items.BLACKSTONE)
                .unlockedBy("has_item", has(ModItems.COLORFUL_PORTAL_FRAME.get()))
                .save(consumer, locCrafting("portal_frame"));

        for (DyeColor color: DyeColor.values()) {
            ShapedRecipeBuilder.shaped(ModItems.DENSE_DYES.get(color).get(), 1)
                    .pattern("###")
                    .pattern("#x#")
                    .pattern("###")
                    .define('#', ColorUtils.getDyeItemByColor(color))
                    .define('x', ModItems.COLORED_INGOTS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.DENSE_DYES.get(color).get()))
                    .save(consumer, locCrafting("dense_dye_" + color.getName()));

            ShapedRecipeBuilder.shaped(ModItems.COLORFUL_COLORED_PORTAL_FRAMES.get(color).get(), 1)
                    .pattern("#o#")
                    .pattern("oxo")
                    .pattern("#o#")
                    .define('#', Items.NETHERITE_INGOT)
                    .define('o', ModItems.DENSE_DYES.get(color).get())
                    .define('x', ModItems.COLORED_NETHER_STARS.get(color).get())
                    .unlockedBy("has_item", has(ModItems.COLORFUL_COLORED_PORTAL_FRAMES.get(color).get()))
                    .save(consumer, locCrafting("colored_portal_frame_" + color.getName()));
        }
    }

    private void resetNBTRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider itemProvider) {
        ShapelessRecipeBuilder.shapeless(itemProvider, 1)
                .requires(itemProvider)
                .unlockedBy("has_item", has(itemProvider))
                .save(consumer, locCrafting(itemProvider.asItem().getRegistryName().getPath() + "_reset"));
    }
}
