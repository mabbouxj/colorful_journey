package net.mabbouxj.colorful_journey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class EnergyDyeGeneratorScreen extends ContainerScreen<EnergyDyeGeneratorContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/gui/energy_dye_generator.png");
    private final EnergyDyeGeneratorContainer container;

    public EnergyDyeGeneratorScreen(EnergyDyeGeneratorContainer container, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(container, playerInventory, textComponent);
        this.container = container;
        this.imageHeight = 167;
        this.imageWidth = 176;
        init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (mouseX > (leftPos + 133) && mouseX < (leftPos + 133) + 18 && mouseY > (topPos + 16) && mouseY < (topPos + 16) + 54)
            this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Collections.singletonList(
                    new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(this.container.getEnergy()), StringUtils.numberWithSuffix(this.container.getMaxEnergy()))
            )), mouseX, mouseY);
        if (mouseX > (leftPos + 25) && mouseX < (leftPos + 25) + 18 && mouseY > (topPos + 34) && mouseY < (topPos + 34) + 18)
            this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Collections.singletonList(
                    this.container.getRemainingFuel() <= 0 ?
                            new TranslationTextComponent("screen.colorful_journey.no_fuel") :
                            new TranslationTextComponent("screen.colorful_journey.fuel_time", StringUtils.ticksInHumanReadable(this.container.getRemainingFuel()))
            )), mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        int flameHeight = 13;
        if (this.container.getMaxFuel() > 0) {
            int remaining = (this.container.getRemainingFuel() * flameHeight) / this.container.getMaxFuel();
            this.blit(matrixStack, leftPos + 27, topPos + 36 + flameHeight - remaining, 176, flameHeight - remaining, flameHeight + 1, remaining + 1);
        }

        int maxIngredient = this.container.getMaxIngredient();
        int arrowWidth = 54;
        if (maxIngredient > 0) {
            int remaining = (this.container.getRemainingIngredient() * arrowWidth) / maxIngredient;
            this.blit(matrixStack, leftPos + 61, topPos + 36, 176, 66, arrowWidth - remaining, 16);
        }

        int maxEnergy = this.container.getMaxEnergy();
        int energyHeight = 52;
        int energyWidth = 16;
        if (maxEnergy > 0) {
            int remaining = (this.container.getEnergy() * energyHeight) / maxEnergy;
            this.blit(matrixStack, leftPos + 134, topPos + 17 + energyHeight - remaining, 176, 66 - remaining, energyWidth, remaining + 1);
        }

        int generating = this.container.getCurrentEnergyGeneration();
        if (generating > 0) {
            String perTickString = new TranslationTextComponent("screen.colorful_journey.suffix_energy_per_tick", generating).getString();
            fontRenderer.draw(matrixStack, perTickString, leftPos + 61, topPos + 22, 0xFF808080);
        }

        int remainingTicks = this.container.getRemainingTicks();
        if (remainingTicks > 0) {
            String remainingIngredientString = StringUtils.ticksInHumanReadable(remainingTicks);
            fontRenderer.draw(matrixStack, remainingIngredientString, leftPos + 61, topPos + 58, 0xFF808080);
        }
    }

}
