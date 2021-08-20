package net.mabbouxj.colorful_journey.client.entity.render;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredChickenLayer;
import net.mabbouxj.colorful_journey.client.entity.model.ColoredChickenModel;
import net.mabbouxj.colorful_journey.entities.ColoredChickenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredChickenRenderer extends MobRenderer<ColoredChickenEntity, ColoredChickenModel<ColoredChickenEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/chicken/colored_chicken.png");

    public ColoredChickenRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ColoredChickenModel<>(), 0.3F);
        this.addLayer(new ColoredChickenLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredChickenEntity entity) {
        return TEXTURE;
    }

    @Override
    protected float getBob(ColoredChickenEntity entity, float p_77044_2_) {
        float f = MathHelper.lerp(p_77044_2_, entity.oFlap, entity.flap);
        float f1 = MathHelper.lerp(p_77044_2_, entity.oFlapSpeed, entity.flapSpeed);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

}
