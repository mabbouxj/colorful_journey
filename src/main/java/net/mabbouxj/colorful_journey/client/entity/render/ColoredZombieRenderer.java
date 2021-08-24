package net.mabbouxj.colorful_journey.client.entity.render;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredZombieRenderer extends MobRenderer<ColoredZombieEntity, ZombieModel<ColoredZombieEntity>> {

    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    public ColoredZombieRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ZombieModel<>(0.0F, false), 0.5F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredZombieEntity entity) {
        return ZOMBIE_LOCATION;
    }

}
