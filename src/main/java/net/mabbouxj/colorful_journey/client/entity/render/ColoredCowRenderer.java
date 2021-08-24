package net.mabbouxj.colorful_journey.client.entity.render;

import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredCowEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredCowRenderer extends MobRenderer<ColoredCowEntity, CowModel<ColoredCowEntity>> {

    private static final ResourceLocation COW_LOCATION = new ResourceLocation("textures/entity/cow/cow.png");

    public ColoredCowRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CowModel<>(), 0.7F);
        this.addLayer(new ColoredMobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredCowEntity p_110775_1_) {
        return COW_LOCATION;
    }
}
