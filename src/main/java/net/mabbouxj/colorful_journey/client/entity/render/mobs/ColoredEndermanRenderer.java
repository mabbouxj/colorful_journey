package net.mabbouxj.colorful_journey.client.entity.render.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredHeldBlockLayer;
import net.mabbouxj.colorful_journey.client.entity.layer.ColoredMobLayer;
import net.mabbouxj.colorful_journey.entities.ColoredEndermanEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EndermanEyesLayer;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColoredEndermanRenderer extends MobRenderer<ColoredEndermanEntity, EndermanModel<ColoredEndermanEntity>> {

    private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
    private final Random random = new Random();

    public ColoredEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new EndermanModel<>(0.0F), 0.5F);
        this.addLayer(new EndermanEyesLayer<>(this));
        this.addLayer(new ColoredHeldBlockLayer(this));
        this.addLayer(new ColoredMobLayer<>(this));
    }

    public void render(ColoredEndermanEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        BlockState blockstate = entity.getCarriedBlock();
        EndermanModel<ColoredEndermanEntity> endermanmodel = this.getModel();
        endermanmodel.carrying = blockstate != null;
        endermanmodel.creepy = entity.isCreepy();
        super.render(entity, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public Vector3d getRenderOffset(ColoredEndermanEntity entity, float offset) {
        if (entity.isCreepy()) {
            double d0 = 0.02D;
            return new Vector3d(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
        } else {
            return super.getRenderOffset(entity, offset);
        }
    }

    public ResourceLocation getTextureLocation(ColoredEndermanEntity entity) {
        return ENDERMAN_LOCATION;
    }

}
