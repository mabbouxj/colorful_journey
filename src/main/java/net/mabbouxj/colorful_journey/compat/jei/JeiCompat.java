package net.mabbouxj.colorful_journey.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.client.gui.WashingMachineScreen;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

@JeiPlugin
public class JeiCompat implements IModPlugin {

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        final IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new WashingMachineCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        IIngredientManager ingredientManager = registration.getIngredientManager();
        IIngredientType<ItemStack> itemType = ingredientManager.getIngredientType(ItemStack.class);

        registration.addRecipes(recipeManager.getAllRecipesFor(ModRecipeTypes.WASHING_MACHINE), new ResourceLocation(ColorfulJourney.MOD_ID, "washing_machine"));
        registration.addIngredientInfo(new ItemStack(ModItems.RUBIKS_CUBE.get()), itemType, new TranslationTextComponent("jei.colorful_journey.rubiks_cube"));
        registration.addIngredientInfo(new ItemStack(ModItems.HORSEHAIR.get()), itemType, new TranslationTextComponent("jei.colorful_journey.horsehair"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.WASHING_MACHINE.get()), new ResourceLocation(ColorfulJourney.MOD_ID, "washing_machine"));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(WashingMachineScreen.class, 85, 34, 24, 18, new ResourceLocation(ColorfulJourney.MOD_ID, "washing_machine"));
    }


}
