package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ModRenderTypes extends RenderType {

    private final static ResourceLocation TEXTURE_LASER_CORE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/misc/laser_core.png");
    private final static ResourceLocation TEXTURE_LASER_BEAM = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/misc/laser_beam.png");
    private final static ResourceLocation TEXTURE_LASER_ADDITIVE = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/misc/laser_additive.png");

    public ModRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }

    public static final RenderType RENDERTYPE_LASER_BEAM = create("laser_beam",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(TEXTURE_LASER_BEAM, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    public static final RenderType RENDERTYPE_LASER_ADDITIVE = create("laser_additive",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(TEXTURE_LASER_ADDITIVE, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    public static final RenderType RENDERTYPE_LASER_CORE = create("laser_core",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(TEXTURE_LASER_CORE, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

}
