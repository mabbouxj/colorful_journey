package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.crafting.ColorGunFillRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipeSerializers {

    public static final RegistryObject<IRecipeSerializer<ColorGunFillRecipe>> COLOR_GUN_FILL_RECIPE = Registration.RECIPE_SERIALIZERS.register("color_gun_fill", () -> new SpecialRecipeSerializer<>(ColorGunFillRecipe::new));

    static void register() {
    }

}
