package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredSquidEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SquidModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredSquidRenderer extends MobRenderer<ColoredSquidEntity, SquidModel<ColoredSquidEntity>> {

    private static final ResourceLocation SQUID_LOCATION = new ResourceLocation("textures/entity/squid.png");

    public ColoredSquidRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new SquidModel<>(), 0.7F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    public ResourceLocation getTextureLocation(ColoredSquidEntity entity) {
        return SQUID_LOCATION;
    }

    protected void setupRotations(ColoredSquidEntity entity, MatrixStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        float f = MathHelper.lerp(p_225621_5_, entity.xBodyRotO, entity.xBodyRot);
        float f1 = MathHelper.lerp(p_225621_5_, entity.zBodyRotO, entity.zBodyRot);
        stack.translate(0.0D, 0.5D, 0.0D);
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_225621_4_));
        stack.mulPose(Vector3f.XP.rotationDegrees(f));
        stack.mulPose(Vector3f.YP.rotationDegrees(f1));
        stack.translate(0.0D, (double)-1.2F, 0.0D);
    }

    protected float getBob(ColoredSquidEntity entity, float bob) {
        return MathHelper.lerp(bob, entity.oldTentacleAngle, entity.tentacleAngle);
    }

}
