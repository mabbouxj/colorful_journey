package net.mabbouxj.colorful_journey.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.EaselBlock;
import net.mabbouxj.colorful_journey.client.entity.model.EaselModel;
import net.mabbouxj.colorful_journey.tiles.EaselTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EaselRenderer extends TileEntityRenderer<EaselTile> {

    private final EaselModel MODEL = new EaselModel();
    private final ResourceLocation STRUCTURE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel.png");

    public EaselRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(EaselTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();

        // Correction for BlockBench model generation position
        matrixStack.translate(0.5D, 1.5D, 0.5D);

        // Apply blockstate rotation
        BlockState blockstate = tile.getBlockState();
        float rotation = (float)(-blockstate.getValue(EaselBlock.ROTATION) * 360) / 16.0F;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));

        // Rotate 180° around Z axis as BlockBench generates models upside down
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));

        // Render structure (wood part)
        RenderType r = MODEL.renderType(STRUCTURE_TEX);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(r);
        MODEL.structure.render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay);


        // Render slate (paper part)
        if (tile.hasSlate) {
            r = MODEL.renderType(EaselTile.EMPTY_SLATE_TEX);
            ivertexbuilder = buffer.getBuffer(r);
            MODEL.slate.render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay);

            if (tile.isFinished()) {
                r = MODEL.renderType(tile.getPaintTexture());
                ivertexbuilder = buffer.getBuffer(r);
                MODEL.paper.paper.render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay);
            }
        }

        matrixStack.popPose();
    }

}
