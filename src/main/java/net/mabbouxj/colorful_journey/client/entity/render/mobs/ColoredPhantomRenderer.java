package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredPhantomEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredPhantomRenderer extends MobRenderer<ColoredPhantomEntity, PhantomModel<ColoredPhantomEntity>> {

    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");

    public ColoredPhantomRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PhantomModel<>(), 0.75F);
        this.addLayer(new PhantomEyesLayer<>(this));
        this.addLayer(new ColoredMobLayer<>(this));
    }

    public ResourceLocation getTextureLocation(ColoredPhantomEntity entity) {
        return PHANTOM_LOCATION;
    }

    protected void scale(PhantomEntity entity, MatrixStack stack, float scale) {
        int i = entity.getPhantomSize();
        float f = 1.0F + 0.15F * (float)i;
        stack.scale(f, f, f);
        stack.translate(0.0D, 1.3125D, 0.1875D);
    }

    protected void setupRotations(ColoredPhantomEntity entity, MatrixStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(entity, stack, p_225621_3_, p_225621_4_, p_225621_5_);
        stack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
    }

}
