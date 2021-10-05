package net.mabbouxj.colorful_journey.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.recipes.WashingMachineRecipe;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WashingMachineCategory implements IRecipeCategory<WashingMachineRecipe>, ITooltipCallback<FluidStack> {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT_1 = 1;
    private static final int OUTPUT_SLOT_2 = 2;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/gui/jei/washing_machine.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable tankOverlay;
    private final IDrawable energyOverlay;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrow;

    public WashingMachineCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 148, 58);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.WASHING_MACHINE.get()));
        this.tankOverlay = guiHelper.createDrawable(TEXTURE, 164, 17, 7, 52);
        this.energyOverlay = guiHelper.createDrawable(TEXTURE, 148, 17, 16, 52);
        this.cachedArrow = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer,IDrawableAnimated>() {
            @Override
            public IDrawableAnimated load(Integer craftProgression) {
                return guiHelper.drawableBuilder(TEXTURE, 149, 0, 23, 17).buildAnimated(craftProgression, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "machines/washing_machine");
    }

    @Override
    @Nonnull
    public Class<? extends WashingMachineRecipe> getRecipeClass() {
        return WashingMachineRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return getTitleAsTextComponent().toString();
    }

    @Override
    @Nonnull
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("block.colorful_journey.washing_machine");
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
    public void setIngredients(WashingMachineRecipe recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.getInputStack());
        ingredients.setInput(VanillaTypes.FLUID, recipe.getInputFluid());
        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(recipe.getResultItem());
        outputs.add(recipe.getResultItemAlt());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull WashingMachineRecipe recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(INPUT_SLOT, true, 56, 20);
        guiItemStacks.init(OUTPUT_SLOT_1, false, 110, 20);
        guiItemStacks.init(OUTPUT_SLOT_2, false, 128, 20);
        guiItemStacks.set(ingredients);

        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.addTooltipCallback(this);
        int capacity = ModConfigs.COMMON.WASHING_MACHINE_FLUID_BUFFER.get();
        fluidStacks.init(0, true, 21, 3, 16, 52, capacity, false, tankOverlay);
        fluidStacks.set(ingredients);
    }

    @Override
    public void draw(@Nonnull WashingMachineRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        cachedArrow.getUnchecked(Math.max(1, recipe.getProcessingTime())).draw(matrixStack, 81, 22);
        energyOverlay.draw(matrixStack, 3, 3);

        drawProcessingTime(recipe, matrixStack, 42);

    }

    protected void drawProcessingTime(WashingMachineRecipe recipe, MatrixStack matrixStack, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            String timeString = StringUtils.ticksInHumanReadable(recipe.getProcessingTime());
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    @Nonnull
    public List<ITextComponent> getTooltipStrings(@Nonnull WashingMachineRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltips = new ArrayList<>();
        if (mouseX > 2 && mouseX < 2 + 18 && mouseY > 2 && mouseY < 2 + 54) {
            int energyPerTick = recipe.getEnergyPerTick();
            int processingTime = recipe.getProcessingTime();
            int totalEnergy = energyPerTick * processingTime;
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy_consume_per_tick", StringUtils.numberWithSuffix(energyPerTick)));
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy_consume_total", StringUtils.numberWithSuffix(totalEnergy)));
        }
        return tooltips;
    }

    @Override
    public boolean handleClick(@Nonnull WashingMachineRecipe recipe, double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean isHandled(WashingMachineRecipe recipe) {
        return recipe.getType() == ModRecipeTypes.WASHING_MACHINE;
    }

    @Override
    public void onTooltip(int i, boolean b, FluidStack fluidStack, List<ITextComponent> list) {
        ITextComponent name = list.get(0);
        ITextComponent modId = list.get(list.size() - 1);
        list.clear();
        list.add(name);
        list.add(new StringTextComponent(StringUtils.numberWithSuffix(fluidStack.getAmount()) + " mB"));
        list.add(modId);
    }
}
