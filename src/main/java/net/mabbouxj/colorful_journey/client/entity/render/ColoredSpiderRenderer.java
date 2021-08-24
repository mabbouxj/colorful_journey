package net.mabbouxj.colorful_journey.client.entity.render;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredSpiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredSpiderRenderer extends MobRenderer<ColoredSpiderEntity, SpiderModel<ColoredSpiderEntity>> {

    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");

    public ColoredSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SpiderModel<>(), 0.8F);
        this.addLayer(new ColoredMobLayer<>(this));
        this.addLayer(new SpiderEyesLayer<>(this));
    }

    protected float getFlipDegrees(ColoredSpiderEntity entity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredSpiderEntity entity) {
        return SPIDER_LOCATION;
    }

}
