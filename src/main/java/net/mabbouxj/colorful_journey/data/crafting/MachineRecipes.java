package net.mabbouxj.colorful_journey.data.crafting;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.data.tag.ModItemTagProvider;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColoredVariantsItem;
import net.mabbouxj.colorful_journey.recipes.EnergyDyeGeneratorRecipe;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

public class MachineRecipes extends RecipeProvider {

    public MachineRecipes(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    public void generate(Consumer<IFinishedRecipe> consumer) {
        energyDyeGeneratorRecipes(consumer);
        washingMachineRecipes(consumer);
    }

    private ResourceLocation locMachine(String name) {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "machines/" + name);
    }

    private void energyDyeGeneratorRecipes(Consumer<IFinishedRecipe> consumer) {
        new EnergyDyeGeneratorRecipe.Builder(locMachine("energy_dye_generator/dyes"))
                .withIngredient(Tags.Items.DYES)
                .withEnergyGeneration(20, 1000)
                .save(consumer);
        new EnergyDyeGeneratorRecipe.Builder(locMachine("energy_dye_generator/dense_dyes"))
                .withIngredient(ModItemTagProvider.DENSE_DYES)
                .withEnergyGeneration(50, 10000)
                .save(consumer);
        new EnergyDyeGeneratorRecipe.Builder(locMachine("energy_dye_generator/ultra_dense_dyes"))
                .withIngredient(ModItemTagProvider.ULTRA_DENSE_DYES)
                .withEnergyGeneration(100, 100000)
                .save(consumer);
    }

    private void washingMachineRecipes(Consumer<IFinishedRecipe> consumer) {
        for (RegistryObject<Item> registryItem: ModItems.ALL_COLORED_VARIANTS_ITEMS) {
            if (registryItem.get() instanceof ColoredVariantsItem) {
                ColoredVariantsItem coloredVariantsItem = (ColoredVariantsItem) registryItem.get();
                if (coloredVariantsItem.getInitialItem() != null) {
                    new WashingMachineRecipe.Builder(locMachine("washing_machine/water_" + registryItem.getId().getPath()))
                            .withInput(registryItem.get(), 1)
                            .withInputFluid(new FluidStack(Fluids.WATER, 100))
                            .withOutput(coloredVariantsItem.getInitialItem(), 1)
                            .withOutputAlt(ColorUtils.getDyeItemByColor(coloredVariantsItem.getColor()), 1)
                            .save(consumer);
                }
            }
        }

        for (DyeColor color: DyeColor.values()) {
            new WashingMachineRecipe.Builder(locMachine("washing_machine/uncolored_ingot_from_" + color.getName()))
                    .withInput(ModItems.COLORED_INGOTS.get(color).get(), 1)
                    .withInputFluid(new FluidStack(Fluids.WATER, 100))
                    .withOutput(ModItems.UNCOLORED_INGOT.get(), 1)
                    .withOutputAlt(ColorUtils.getDyeItemByColor(color), 1)
                    .save(consumer);
            new WashingMachineRecipe.Builder(locMachine("washing_machine/uncolored_ingot_block_from_" + color.getName()))
                    .withInput(ModItems.COLORED_INGOT_BLOCKS.get(color).get(), 1)
                    .withInputFluid(new FluidStack(Fluids.WATER, 900))
                    .withOutput(ModItems.UNCOLORED_INGOT_BLOCK.get(), 1)
                    .withOutputAlt(ColorUtils.getDyeItemByColor(color), 9)
                    .save(consumer);
        }


    }

}
