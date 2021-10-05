package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredBlazeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BlazeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredBlazeRenderer extends MobRenderer<ColoredBlazeEntity, BlazeModel<ColoredBlazeEntity>> {

    private static final ResourceLocation BLAZE_LOCATION = new ResourceLocation("textures/entity/blaze.png");

    public ColoredBlazeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BlazeModel<>(), 0.5F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    protected int getBlockLightLevel(ColoredBlazeEntity entity, BlockPos pos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(ColoredBlazeEntity entity) {
        return BLAZE_LOCATION;
    }


}
