package net.mabbouxj.colorful_journey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.client.utils.RenderUtils;
import net.mabbouxj.colorful_journey.containers.WashingMachineContainer;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class WashingMachineScreen extends ContainerScreen<WashingMachineContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/gui/washing_machine.png");
    private final WashingMachineContainer container;

    public WashingMachineScreen(WashingMachineContainer container, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(container, playerInventory, textComponent);
        this.container = container;
        this.imageHeight = 167;
        this.imageWidth = 176;
        init();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (mouseX > (leftPos + 7) && mouseX < (leftPos + 7) + 18 && mouseY > (topPos + 16) && mouseY < (topPos + 16) + 54)
            this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Arrays.asList(
                    new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(this.container.getEnergy()), StringUtils.numberWithSuffix(this.container.getMaxEnergy()))
            )), mouseX, mouseY);
        if (mouseX > (leftPos + 25) && mouseX < (leftPos + 25) + 18 && mouseY > (topPos + 16) && mouseY < (topPos + 16) + 54)
            this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Arrays.asList(
                    new TranslationTextComponent("screen.colorful_journey.fluid", this.container.getFluidStack().getDisplayName(), StringUtils.numberWithSuffix(this.container.getFluid()), StringUtils.numberWithSuffix(this.container.getMaxFluid()))
            )), mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.getMinecraft().getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        int maxEnergy = this.container.getMaxEnergy();
        int energyHeight = 52;
        int energyWidth = 16;
        if (maxEnergy > 0) {
            int remaining = (this.container.getEnergy() * energyHeight) / maxEnergy;
            this.blit(matrixStack, leftPos + 8, topPos + 17 + energyHeight - remaining, 176, 69 - remaining, energyWidth, remaining + 1);
        }

        int maxProgress = this.container.getMaxProgress();
        int arrowHeight = 17;
        int arrowWidth = 24;
        if (maxProgress > 0) {
            int remaining = (this.container.getProgress() * arrowWidth) / maxProgress;
            this.blit(matrixStack, leftPos + 85, topPos + 36, 176, 0, remaining + 1, arrowHeight);
        }

        renderFluid();

        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.getMinecraft().getTextureManager().bind(TEXTURE);
        renderFluidBars(matrixStack);

    }

    private void renderFluid() {
        FluidStack fluidStack = this.container.getFluidStack();
        int fluidHeight = 52;
        int fluidWidth = 16;

        if (!fluidStack.isEmpty()) {
            int remaining = (this.container.getFluid() * fluidHeight) / this.container.getMaxFluid();
            RenderUtils.drawFluid(leftPos + 26, topPos + 17 + fluidHeight - remaining, fluidStack, fluidWidth, remaining);
        }
    }

    private void renderFluidBars(MatrixStack matrixStack) {
        this.blit(matrixStack, leftPos + 26, topPos + 17, 192, 17, 16, 52);
    }

}
