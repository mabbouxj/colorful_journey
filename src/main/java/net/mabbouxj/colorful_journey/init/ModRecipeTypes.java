package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ModRecipeTypes {

    public static IRecipeType<WashingMachineRecipe> WASHING_MACHINE = register("washing_machine");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(name), new IRecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }

}
