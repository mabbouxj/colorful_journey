package net.mabbouxj.colorful_journey.data.crafting;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        new WoodRecipes(this.generator).generate(consumer);
        new MetalRecipes(this.generator).generate(consumer);
        new MachineRecipes(this.generator).generate(consumer);
        new CraftingRecipes(this.generator).generate(consumer);

    }
}
