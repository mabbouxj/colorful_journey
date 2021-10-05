package net.mabbouxj.colorful_journey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.containers.EaselContainer;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class EaselScreen extends ContainerScreen<EaselContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/gui/easel.png");
    private final EaselContainer container;

    public EaselScreen(EaselContainer container, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(container, playerInventory, textComponent);
        this.container = container;
        this.imageHeight = 184;
        this.imageWidth = 176;
        this.inventoryLabelY = this.inventoryLabelY + 18;
        init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int selectedColor = this.container.getSelectedColor();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

        if (this.container.hasUsableColorPalette()) {
            if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 16 && mouseY < topPos + 16+18 && selectedColor != DyeColor.RED.getId()) {
                this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Collections.singletonList(
                        new TranslationTextComponent("screen.colorful_journey.select_color", ColorUtils.coloredColorName(DyeColor.RED))
                )), mouseX, mouseY);
            } else if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 34 && mouseY < topPos + 34+18 && selectedColor != DyeColor.GREEN.getId()) {
                this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Collections.singletonList(
                        new TranslationTextComponent("screen.colorful_journey.select_color", ColorUtils.coloredColorName(DyeColor.GREEN))
                )), mouseX, mouseY);
            } else if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 52 && mouseY < topPos + 52+18 && selectedColor != DyeColor.BLUE.getId()) {
                this.renderTooltip(matrixStack, LanguageMap.getInstance().getVisualOrder(Collections.singletonList(
                        new TranslationTextComponent("screen.colorful_journey.select_color", ColorUtils.coloredColorName(DyeColor.BLUE))
                )), mouseX, mouseY);
            }
        }

    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.container.hasUsableColorPalette()) {
            renderColorSelector(matrixStack);
        }
        if (this.container.isFinished()) {
            this.blit(matrixStack, leftPos + 97, topPos + 34, 212, 0, 18, 18);
        }

        if (this.container.hasSlate()) {
            renderPatternGrid(matrixStack, this.container.getPatternBeingPainted(), 44);
            renderPatternGrid(matrixStack, this.container.getPatternToReproduce(), 116);
            if (mouseX > leftPos + 43 && mouseX < leftPos + 97 && mouseY > topPos + 16 && mouseY < topPos + 70){
                this.renderMouseHoveringPattern(matrixStack, mouseX, mouseY);
            }
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.container.hasUsableColorPalette()) {
            if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 16 && mouseY < topPos + 16+18) {
                this.container.setSelectedColor(DyeColor.RED.getId());
            } else if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 34 && mouseY < topPos + 34+18) {
                this.container.setSelectedColor(DyeColor.GREEN.getId());
            } else if (mouseX > leftPos + 7 && mouseX < leftPos + 7+18 && mouseY > topPos + 52 && mouseY < topPos + 52+18) {
                this.container.setSelectedColor(DyeColor.BLUE.getId());
            } else if (mouseX > leftPos + 43 && mouseX < leftPos + 97 && mouseY > topPos + 16 && mouseY < topPos + 70) {
                handlePaintingClick((int)mouseX, (int)mouseY, mouseButton);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void handlePaintingClick(int mouseX, int mouseY, int mouseButton) {
        if (!this.container.hasSlate() || this.container.isFinished())
            return;
        int selectedColor = this.container.getSelectedColor();
        int[] pattern = this.container.getPatternBeingPainted();

        mouseX = Math.floorDiv((mouseX - leftPos - 43), 18);
        mouseY = Math.floorDiv((mouseY - topPos - 16), 18);

        if (mouseX >= 0 && mouseX <= 2 && mouseY >= 0 && mouseY <= 2) {
            int clickedPatternIndex = mouseX + 3 * mouseY;
            int newColor = -1;
            int damage = 0;
            InputMappings.Input mouseKey = InputMappings.Type.MOUSE.getOrCreate(mouseButton);
            if (!Minecraft.getInstance().options.keyUse.isActiveAndMatches(mouseKey)) {
                int initialColor = pattern[clickedPatternIndex];
                newColor = mixColors(initialColor, selectedColor);
                damage = 1;
            }

            pattern[clickedPatternIndex] = newColor;
            this.container.setPatternBeingPaintedAndDamageColorPalette(pattern, damage);
        }

    }

    private int mixColors(int initial, int rgb) {
        if (initial < 0 || initial > DyeColor.values().length) return rgb;
        if (rgb < 0 || rgb > DyeColor.values().length) return initial;

        if (rgb == DyeColor.RED.getId() && initial == DyeColor.RED.getId()) return DyeColor.RED.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.GREEN.getId()) return DyeColor.YELLOW.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.BLUE.getId()) return DyeColor.MAGENTA.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.MAGENTA.getId()) return DyeColor.PINK.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.CYAN.getId()) return DyeColor.WHITE.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.YELLOW.getId()) return DyeColor.ORANGE.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.WHITE.getId()) return DyeColor.RED.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.PURPLE.getId()) return DyeColor.MAGENTA.getId();
        if (rgb == DyeColor.RED.getId() && initial == DyeColor.ORANGE.getId()) return DyeColor.RED.getId();

        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.RED.getId()) return DyeColor.YELLOW.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.GREEN.getId()) return DyeColor.GREEN.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.BLUE.getId()) return DyeColor.CYAN.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.MAGENTA.getId()) return DyeColor.WHITE.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.CYAN.getId()) return DyeColor.GREEN.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.YELLOW.getId()) return DyeColor.LIME.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.WHITE.getId()) return DyeColor.GREEN.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.PURPLE.getId()) return DyeColor.CYAN.getId();
        if (rgb == DyeColor.GREEN.getId() && initial == DyeColor.ORANGE.getId()) return DyeColor.YELLOW.getId();

        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.RED.getId()) return DyeColor.MAGENTA.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.GREEN.getId()) return DyeColor.CYAN.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.BLUE.getId()) return DyeColor.BLUE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.MAGENTA.getId()) return DyeColor.PURPLE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.CYAN.getId()) return DyeColor.LIGHT_BLUE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.YELLOW.getId()) return DyeColor.WHITE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.WHITE.getId()) return DyeColor.BLUE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.PURPLE.getId()) return DyeColor.BLUE.getId();
        if (rgb == DyeColor.BLUE.getId() && initial == DyeColor.ORANGE.getId()) return DyeColor.MAGENTA.getId();

        return rgb;
    }

    private void renderColorSelector(MatrixStack matrixStack) {
        int selectedColor = this.container.getSelectedColor();
        if (selectedColor == DyeColor.RED.getId()) {
            this.blit(matrixStack, leftPos + 7, topPos + 16, 176, 0, 18, 18);
        } else if (selectedColor == DyeColor.GREEN.getId()) {
            this.blit(matrixStack, leftPos + 7, topPos + 16 + 18, 176, 18, 18, 18);
        } else if (selectedColor == DyeColor.BLUE.getId()) {
            this.blit(matrixStack, leftPos + 7, topPos + 16 + 36, 176, 36, 18, 18);
        }
    }

    private void renderPatternGrid(MatrixStack matrixStack, int[] pattern, int xOffset) {
        if (pattern.length != 9) return;
        for (int i = 0; i < 9; i++) {
            int colorId = pattern[i];
            if (colorId < 0 || colorId > DyeColor.values().length)
                continue;
            int colorX = colorId * 16;
            int targetX = Math.floorMod(i, 3) * 18;
            int targetY = Math.floorDiv(i, 3) * 18;
            this.blit(matrixStack, leftPos + xOffset + targetX, topPos + 17 + targetY, colorX, 184, 16, 16);
        }
    }

    private void renderMouseHoveringPattern(MatrixStack matrixStack, int mouseX, int mouseY) {
        int selectedColor = this.container.getSelectedColor();
        int colorY = selectedColor == DyeColor.RED.getId() ? 0: selectedColor == DyeColor.GREEN.getId() ? 18: 36;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = leftPos + 43 + i*18;
                int y = topPos + 16 + j*18;
                if (mouseX > x && mouseX < x+18 && mouseY > y && mouseY < y+18) {
                    this.blit(matrixStack, x, y, 194, colorY, 18, 18);
                }
            }
        }
    }

}
