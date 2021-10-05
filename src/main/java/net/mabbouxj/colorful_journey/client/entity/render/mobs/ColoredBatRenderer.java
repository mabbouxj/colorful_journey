package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.client.entity.model.ColoredBatModel;
import net.mabbouxj.colorful_journey.entities.ColoredBatEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredBatRenderer extends MobRenderer<ColoredBatEntity, ColoredBatModel> {

    private static final ResourceLocation BAT_LOCATION = new ResourceLocation("textures/entity/bat.png");

    public ColoredBatRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ColoredBatModel(), 0.25F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    public ResourceLocation getTextureLocation(ColoredBatEntity entity) {
        return BAT_LOCATION;
    }

    protected void scale(ColoredBatEntity entity, MatrixStack matrixStack, float scale) {
        matrixStack.scale(0.35F, 0.35F, 0.35F);
    }

    protected void setupRotations(ColoredBatEntity entity, MatrixStack matrixStack, float x, float y, float z) {
        if (entity.isResting()) {
            matrixStack.translate(0.0D, -0.1F, 0.0D);
        } else {
            matrixStack.translate(0.0D, (MathHelper.cos(x * 0.3F) * 0.1F), 0.0D);
        }

        super.setupRotations(entity, matrixStack, x, y, z);
    }

}
