package net.mabbouxj.colorful_journey.client.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.client.entity.model.ColoredChickenModel;
import net.mabbouxj.colorful_journey.entities.ColoredChickenEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class ColoredChickenLayer extends LayerRenderer<ColoredChickenEntity, ColoredChickenModel<ColoredChickenEntity>> {

    private static final ResourceLocation CHICKEN_LAYER_LOCATION = new ResourceLocation(Reference.MOD_ID, "textures/entity/chicken/colored_chicken_layer.png");
    private final ColoredChickenModel<ColoredChickenEntity> model = new ColoredChickenModel<>();

    public ColoredChickenLayer(IEntityRenderer<ColoredChickenEntity, ColoredChickenModel<ColoredChickenEntity>> model) {
        super(model);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, ColoredChickenEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!entity.isInvisible()) {
            float[] afloat = Reference.COLORARRAY_BY_COLOR.get(entity.getColor());
            if (afloat == null) {
                return;
            }
            float f = afloat[0];
            float f1 = afloat[1];
            float f2 = afloat[2];
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, CHICKEN_LAYER_LOCATION, matrixStack, buffer, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, f, f1, f2);
        }
    }

}
