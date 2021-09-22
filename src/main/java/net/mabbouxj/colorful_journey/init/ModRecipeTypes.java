package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipeTypes {

    public static IRecipeType<WashingMachineRecipe> WASHING_MACHINE = register("washing_machine");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String name) {
        return IRecipeType.register(ColorfulJourney.MOD_ID + ":" + name);
    }

}
