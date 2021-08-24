package net.mabbouxj.colorful_journey.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredEndermanEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EndermanEyesLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColoredEndermanRenderer extends MobRenderer<ColoredEndermanEntity, EndermanModel<ColoredEndermanEntity>> {

    private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
    private final Random random = new Random();

    public ColoredEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new EndermanModel<>(0.0F), 0.5F);
        this.addLayer(new ColoredMobLayer<>(this));
        this.addLayer(new EndermanEyesLayer<>(this));
        this.addLayer(new ColoredHeldBlockLayer(this));
    }

    public void render(ColoredEndermanEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        BlockState blockstate = entity.getCarriedBlock();
        EndermanModel<ColoredEndermanEntity> endermanmodel = this.getModel();
        endermanmodel.carrying = blockstate != null;
        endermanmodel.creepy = entity.isCreepy();
        super.render(entity, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public Vector3d getRenderOffset(ColoredEndermanEntity entity, float p_225627_2_) {
        if (entity.isCreepy()) {
            double d0 = 0.02D;
            return new Vector3d(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
        } else {
            return super.getRenderOffset(entity, p_225627_2_);
        }
    }

    public ResourceLocation getTextureLocation(ColoredEndermanEntity entity) {
        return ENDERMAN_LOCATION;
    }

    @OnlyIn(Dist.CLIENT)
    protected class ColoredHeldBlockLayer extends LayerRenderer<ColoredEndermanEntity, EndermanModel<ColoredEndermanEntity>> {
        public ColoredHeldBlockLayer(IEntityRenderer<ColoredEndermanEntity, EndermanModel<ColoredEndermanEntity>> p_i50949_1_) {
            super(p_i50949_1_);
        }

        public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, ColoredEndermanEntity p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
            BlockState blockstate = p_225628_4_.getCarriedBlock();
            if (blockstate != null) {
                p_225628_1_.pushPose();
                p_225628_1_.translate(0.0D, 0.6875D, -0.75D);
                p_225628_1_.mulPose(Vector3f.XP.rotationDegrees(20.0F));
                p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(45.0F));
                p_225628_1_.translate(0.25D, 0.1875D, 0.25D);
                float f = 0.5F;
                p_225628_1_.scale(-0.5F, -0.5F, 0.5F);
                p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, p_225628_1_, p_225628_2_, p_225628_3_, OverlayTexture.NO_OVERLAY);
                p_225628_1_.popPose();
            }
        }
    }

}
