package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.crafting.ColorGunFillRecipe;
import net.mabbouxj.colorful_journey.crafting.ColorPaletteFillRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ColorGunFillRecipe>> COLOR_GUN_FILL_RECIPE = RECIPE_SERIALIZERS.register("color_gun_fill", () -> new SpecialRecipeSerializer<>(ColorGunFillRecipe::new));
    public static final RegistryObject<IRecipeSerializer<ColorPaletteFillRecipe>> COLOR_PALETTE_FILL_RECIPE = RECIPE_SERIALIZERS.register("color_palette_fill", () -> new SpecialRecipeSerializer<>(ColorPaletteFillRecipe::new));

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }

}
