package net.mabbouxj.colorful_journey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

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
        if (mouseX > (leftPos + 98) && mouseX < (leftPos + 98) + 18 && mouseY > (topPos + 20) && mouseY < (topPos + 20) + 53)
            this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Arrays.asList(
                    new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(this.container.getEnergy()), StringUtils.numberWithSuffix(this.container.getMaxPower())),
                    this.container.getRemainingFuel() <= 0 ?
                            new TranslationTextComponent("screen.colorful_journey.no_fuel") :
                            new TranslationTextComponent("screen.colorful_journey.fuel_time", StringUtils.ticksInSeconds(this.container.getRemainingFuel())),
                    this.container.getRemainingMaterial() <= 0 ?
                            new TranslationTextComponent("screen.colorful_journey.no_material") :
                            new TranslationTextComponent("screen.colorful_journey.material_time", StringUtils.numberWithSuffix(this.container.getRemainingFuel()))
            )), mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        int flameHeight = 13;
        if (this.container.getMaxFuel() > 0) {
            int remaining = (this.container.getRemainingFuel() * flameHeight) / this.container.getMaxFuel();
            this.blit(matrixStack, leftPos + 63, topPos + 40 + flameHeight - remaining, 176, flameHeight - remaining, 14, remaining + 1);
        }

        int maxEnergy = this.container.getMaxPower();
        int energyHeight = 51;
        int energyWidth = 16;
        if (maxEnergy > 0) {
            int remaining = (this.container.getEnergy() * energyHeight) / maxEnergy;
            this.blit(matrixStack, leftPos + 99, topPos + 21 + energyHeight - remaining, 176, 65 - remaining, energyWidth, remaining + 1);
        }
    }

}
