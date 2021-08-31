package net.mabbouxj.colorful_journey.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredWitherAuraLayer;
import net.mabbouxj.colorful_journey.entities.ColoredWitherEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.WitherModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredWitherRenderer extends MobRenderer<ColoredWitherEntity, WitherModel<ColoredWitherEntity>> {

    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");

    public ColoredWitherRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WitherModel<>(0.0F), 1.0F);
        this.addLayer(new ColoredWitherAuraLayer(this));
        this.addLayer(new ColoredMobLayer<>(this));
    }

    protected int getBlockLightLevel(ColoredWitherEntity entity, BlockPos blockPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(ColoredWitherEntity entity) {
        int i = entity.getInvulnerableTicks();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }

    protected void scale(ColoredWitherEntity entity, MatrixStack matrixStack, float scale) {
        float f = 2.0F;
        int i = entity.getInvulnerableTicks();
        if (i > 0) {
            f -= ((float) i - scale) / 220.0F * 0.5F;
        }
        matrixStack.scale(f, f, f);
    }

}
