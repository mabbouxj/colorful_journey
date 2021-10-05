package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredGhastEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.GhastModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredGhastRenderer extends MobRenderer<ColoredGhastEntity, GhastModel<ColoredGhastEntity>> {

    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public ColoredGhastRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GhastModel<>(), 1.5F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    public ResourceLocation getTextureLocation(ColoredGhastEntity p_110775_1_) {
        return p_110775_1_.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
    }

    protected void scale(ColoredGhastEntity entity, MatrixStack stack, float scale) {
        float f = 1.0F;
        float f1 = 4.5F;
        float f2 = 4.5F;
        stack.scale(4.5F, 4.5F, 4.5F);
    }

}
