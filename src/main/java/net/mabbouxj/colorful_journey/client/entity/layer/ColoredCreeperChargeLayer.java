package net.mabbouxj.colorful_journey.client.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mabbouxj.colorful_journey.entities.ColoredCreeperEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class ColoredCreeperChargeLayer extends EnergyLayer<ColoredCreeperEntity, CreeperModel<ColoredCreeperEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final CreeperModel<ColoredCreeperEntity> model = new CreeperModel<>(2.0F);

    public ColoredCreeperChargeLayer(IEntityRenderer<ColoredCreeperEntity, CreeperModel<ColoredCreeperEntity>> entityRenderer) {
        super(entityRenderer);
    }

    protected float xOffset(float offset) {
        return offset * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<ColoredCreeperEntity> model() {
        return this.model;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, ColoredCreeperEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.isPowered()) {
            float[] rgb = entity.getColor().getTextureDiffuseColors();
            float f = (float)entity.tickCount + p_225628_7_;
            EntityModel<ColoredCreeperEntity> entitymodel = this.model();
            entitymodel.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.getParentModel().copyPropertiesTo(entitymodel);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f), f * 0.01F));
            entitymodel.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            entitymodel.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
        }
    }
}