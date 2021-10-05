package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredBeeRenderer extends MobRenderer<ColoredBeeEntity, BeeModel<ColoredBeeEntity>> {

    private static final ResourceLocation ANGRY_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry.png");
    private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry_nectar.png");
    private static final ResourceLocation BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee.png");
    private static final ResourceLocation NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_nectar.png");

    public ColoredBeeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BeeModel<>(), 0.4F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredBeeEntity entity) {
        if (entity.isAngry()) {
            return entity.hasNectar() ? ANGRY_NECTAR_BEE_TEXTURE : ANGRY_BEE_TEXTURE;
        } else {
            return entity.hasNectar() ? NECTAR_BEE_TEXTURE : BEE_TEXTURE;
        }
    }

}
