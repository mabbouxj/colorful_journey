package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredWitherSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredWitherSkeletonRenderer extends MobRenderer<ColoredWitherSkeletonEntity, SkeletonModel<ColoredWitherSkeletonEntity>> {

    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public ColoredWitherSkeletonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SkeletonModel<>(), 0.5F);
        this.addLayer(new HeadLayer<>(this, 1.0F, 1.0F, 1.0F));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredWitherSkeletonEntity entity) {
        return WITHER_SKELETON_LOCATION;
    }

    @Override
    protected void scale(ColoredWitherSkeletonEntity entity, MatrixStack matrixStack, float scale) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
    }

}