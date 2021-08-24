package net.mabbouxj.colorful_journey.client.entity.render;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredSkeletonRenderer extends MobRenderer<ColoredSkeletonEntity, SkeletonModel<ColoredSkeletonEntity>> {

    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public ColoredSkeletonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SkeletonModel<>(), 0.4F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredSkeletonEntity entity) {
        return SKELETON_LOCATION;
    }

}
