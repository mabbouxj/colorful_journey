package net.mabbouxj.colorful_journey.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredCreeperChargeLayer;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredCreeperEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ColoredCreeperRenderer extends MobRenderer<ColoredCreeperEntity, CreeperModel<ColoredCreeperEntity>> {

    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public ColoredCreeperRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new CreeperModel<>(), 0.5F);
        this.addLayer(new ColoredCreeperChargeLayer(this));
        this.addLayer(new ColoredMobLayer<>(this));
    }

    protected void scale(ColoredCreeperEntity entity, MatrixStack matrixStack, float scale) {
        float f = entity.getSwelling(scale);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStack.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(ColoredCreeperEntity entity, float p_225625_2_) {
        float f = entity.getSwelling(p_225625_2_);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(ColoredCreeperEntity p_110775_1_) {
        return CREEPER_LOCATION;
    }

}
