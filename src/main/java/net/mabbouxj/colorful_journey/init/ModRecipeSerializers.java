package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.recipes.ColorGunFillRecipe;
import net.mabbouxj.colorful_journey.recipes.ColorPaletteFillRecipe;
import net.mabbouxj.colorful_journey.recipes.EnergyDyeGeneratorRecipe;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ColorGunFillRecipe>> COLOR_GUN_FILL = RECIPE_SERIALIZERS.register("color_gun_fill", () -> new SpecialRecipeSerializer<>(ColorGunFillRecipe::new));
    public static final RegistryObject<IRecipeSerializer<ColorPaletteFillRecipe>> COLOR_PALETTE_FILL = RECIPE_SERIALIZERS.register("color_palette_fill", () -> new SpecialRecipeSerializer<>(ColorPaletteFillRecipe::new));
    public static final RegistryObject<IRecipeSerializer<WashingMachineRecipe>> WASHING_MACHINE = RECIPE_SERIALIZERS.register("washing_machine", WashingMachineRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<EnergyDyeGeneratorRecipe>> ENERGY_DYE_GENERATOR = RECIPE_SERIALIZERS.register("energy_dye_generator", EnergyDyeGeneratorRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }

}
