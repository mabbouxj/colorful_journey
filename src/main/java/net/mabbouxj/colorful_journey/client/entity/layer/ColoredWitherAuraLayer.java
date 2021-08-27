package net.mabbouxj.colorful_journey.client.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.ColoredWitherEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.WitherModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredWitherAuraLayer extends EnergyLayer<ColoredWitherEntity, WitherModel<ColoredWitherEntity>> {

    private static final ResourceLocation WITHER_ARMOR_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/wither/colored_wither_armor.png");
    private final WitherModel<ColoredWitherEntity> model = new WitherModel<>(0.5F);

    public ColoredWitherAuraLayer(IEntityRenderer<ColoredWitherEntity, WitherModel<ColoredWitherEntity>> renderer) {
        super(renderer);
    }

    protected float xOffset(float offset) {
        return MathHelper.cos(offset * 0.02F) * 3.0F;
    }

    protected ResourceLocation getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    protected EntityModel<ColoredWitherEntity> model() {
        return this.model;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, ColoredWitherEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.isPowered()) {
            float[] rgb = entity.getColor().getTextureDiffuseColors();
            float f = (float) entity.tickCount + p_225628_7_;
            EntityModel<ColoredWitherEntity> entitymodel = this.model();
            entitymodel.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.getParentModel().copyPropertiesTo(entitymodel);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f), f * 0.01F));
            entitymodel.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            entitymodel.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
        }
    }
}
