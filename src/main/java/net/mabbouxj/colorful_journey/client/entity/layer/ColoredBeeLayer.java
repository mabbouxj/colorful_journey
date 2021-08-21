package net.mabbouxj.colorful_journey.client.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.entities.ColoredBeeEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredBeeLayer extends LayerRenderer<ColoredBeeEntity, BeeModel<ColoredBeeEntity>> {

    private static final ResourceLocation COLORED_LAYER_TEXTURE_LOCATION = new ResourceLocation(Reference.MOD_ID, "textures/entity/bee/colored_bee_layer.png");
    private final BeeModel<ColoredBeeEntity> model = new BeeModel<>();

    public ColoredBeeLayer(IEntityRenderer<ColoredBeeEntity, BeeModel<ColoredBeeEntity>> model) {
        super(model);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, ColoredBeeEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!entity.isInvisible()) {
            float[] afloat = Reference.COLORARRAY_BY_COLOR.get(entity.getColor());
            if (afloat == null) {
                return;
            }
            float f = afloat[0];
            float f1 = afloat[1];
            float f2 = afloat[2];
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, COLORED_LAYER_TEXTURE_LOCATION, matrixStack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, f, f1, f2);
        }
    }

}