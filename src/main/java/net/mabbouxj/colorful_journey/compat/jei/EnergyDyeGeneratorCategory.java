package net.mabbouxj.colorful_journey.compat.jei;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
    private final IDrawableAnimated energy;
    private final IDrawableAnimated fire;
    private final IDrawableAnimated arrow;

    public EnergyDyeGeneratorCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 130, 58);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.ENERGY_DYE_GENERATOR.get()));
        fire = guiHelper.drawableBuilder(TEXTURE, 130, 0, 14, 14)
                .buildAnimated(new TickTimer(guiHelper, 14), IDrawableAnimated.StartDirection.BOTTOM);
        energy = guiHelper.drawableBuilder(TEXTURE, 130, 14, 16, 52)
                .buildAnimated(new TickTimer(guiHelper, 52), IDrawableAnimated.StartDirection.BOTTOM);
        arrow = guiHelper.drawableBuilder(TEXTURE, 130, 66, 54, 16)
                .buildAnimated(new TickTimer(guiHelper, 54), IDrawableAnimated.StartDirection.LEFT);
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
        energy.draw(matrixStack, 111, 3);
        fire.draw(matrixStack, 4, 22);
        arrow.draw(matrixStack, 38, 22);
        drawEnergyGeneration(recipe, matrixStack);
    }

    protected void drawEnergyGeneration(EnergyDyeGeneratorRecipe recipe, MatrixStack matrixStack) {
        FontRenderer fontRenderer = Minecraft.getInstance().font;

        int perTickEnergy = recipe.energyPerTick;
        int totalEnergy = recipe.energyTotal;
        int totalTicks = totalEnergy / perTickEnergy;

        String perTickString = new TranslationTextComponent("screen.colorful_journey.suffix_energy_per_tick", StringUtils.numberWithSuffix(perTickEnergy)).getString();
        fontRenderer.draw(matrixStack, perTickString, 38, 7, 0xFF808080);

        String totalTicksString = StringUtils.ticksInHumanReadable(totalTicks);
        fontRenderer.draw(matrixStack, totalTicksString, 38, 43, 0xFF808080);
    }

    @Override
    @Nonnull
    public List<ITextComponent> getTooltipStrings(@Nonnull EnergyDyeGeneratorRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltips = new ArrayList<>();
        if (mouseX > 110 && mouseX < 110+18 && mouseY > 2 && mouseY < 2+54)
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.suffix_energy", StringUtils.numberWithSuffix(recipe.energyTotal)));
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
