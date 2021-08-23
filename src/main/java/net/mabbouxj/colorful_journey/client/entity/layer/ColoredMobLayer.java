package net.mabbouxj.colorful_journey.client.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.entities.IColoredMobEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredMobLayer<T extends MobEntity & IColoredMobEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {

    public ColoredMobLayer(IEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!entity.isInvisible()) {
            float[] rgb = entity.getColor().getTextureDiffuseColors();
            coloredCutoutModelCopyLayerRender(this.getParentModel(), entity.getEntityModel(), entity.getLayerLocation(), matrixStack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, rgb[0], rgb[1], rgb[2]);
        }
    }

}
