package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredChickenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredChickenRenderer extends MobRenderer<ColoredChickenEntity, ChickenModel<ColoredChickenEntity>> {

    private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/chicken.png");

    public ColoredChickenRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ChickenModel<>(), 0.3F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredChickenEntity entity) {
        return CHICKEN_LOCATION;
    }

    @Override
    protected float getBob(ColoredChickenEntity entity, float p_77044_2_) {
        float f = MathHelper.lerp(p_77044_2_, entity.oFlap, entity.flap);
        float f1 = MathHelper.lerp(p_77044_2_, entity.oFlapSpeed, entity.flapSpeed);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

}
