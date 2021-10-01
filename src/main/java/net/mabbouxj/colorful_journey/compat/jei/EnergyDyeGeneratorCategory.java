package net.mabbouxj.colorful_journey.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.recipes.EnergyDyeGeneratorRecipe;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnergyDyeGeneratorCategory implements IRecipeCategory<EnergyDyeGeneratorRecipe> {

    private static final int INPUT_SLOT = 0;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/gui/jei/energy_dye_generator.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyOverlay;
    private final LoadingCache<Integer, IDrawableAnimated> cachedFire;

    public EnergyDyeGeneratorCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 58, 58);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.ENERGY_DYE_GENERATOR.get()));
        this.energyOverlay = guiHelper.createDrawable(TEXTURE, 58, 14, 16, 52);
        this.cachedFire = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer,IDrawableAnimated>() {
            @Override
            public IDrawableAnimated load(Integer craftProgression) {
                return guiHelper.drawableBuilder(TEXTURE, 58, 0, 14, 14).buildAnimated(craftProgression, IDrawableAnimated.StartDirection.BOTTOM, false);
            }
        });
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "machines/energy_dye_generator");
    }

    @Override
    @Nonnull
    public Class<? extends EnergyDyeGeneratorRecipe> getRecipeClass() {
        return EnergyDyeGeneratorRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return getTitleAsTextComponent().toString();
    }

    @Override
    @Nonnull
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("block.colorful_journey.energy_dye_generator");
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(EnergyDyeGeneratorRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.ingredient));
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull EnergyDyeGeneratorRecipe recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(INPUT_SLOT, true, 2, 2);
        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(@Nonnull EnergyDyeGeneratorRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        cachedFire.getUnchecked(Math.max(1, 100)).draw(matrixStack, 3, 22);
        energyOverlay.draw(matrixStack, 39, 3);
    }

    @Override
    @Nonnull
    public List<ITextComponent> getTooltipStrings(@Nonnull EnergyDyeGeneratorRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltips = new ArrayList<>();
        if (mouseX > 38 && mouseX < 38 + 18 && mouseY > 2 && mouseY < 2 + 54) {
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.generating", StringUtils.numberWithSuffix(recipe.energyPerTick)));
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy_consume_total", StringUtils.numberWithSuffix(recipe.energyTotal)));
        }
        return tooltips;
    }

    @Override
    public boolean handleClick(@Nonnull EnergyDyeGeneratorRecipe recipe, double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean isHandled(EnergyDyeGeneratorRecipe recipe) {
        return recipe.getType() == ModRecipeTypes.ENERGY_DYE_GENERATOR;
    }

}
