package net.mabbouxj.colorful_journey.client.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mabbouxj.colorful_journey.init.ModRenderTypes;
import net.mabbouxj.colorful_journey.items.ColorLaserGunItem;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static net.mabbouxj.colorful_journey.items.ColorLaserGunItem.LASER_RANGE;
import static net.mabbouxj.colorful_journey.items.ColorLaserGunItem.LASER_THICKNESS;

@OnlyIn(Dist.CLIENT)
public class LaserBeamRenderer {

    public static void renderLaser(RenderWorldLastEvent event, PlayerEntity player, float ticks) {
        ItemStack stack = ColorLaserGunItem.getGun(player);

        if (stack == ItemStack.EMPTY)
            return;

        int range = LASER_RANGE;
        float speedModifier = -0.02f;

        Vector3d playerPos = player.getEyePosition(ticks);
        RayTraceResult trace = player.pick(range, 0.0F, false);

        drawLasers(stack, event, playerPos, trace, 0, 0, 0,LASER_THICKNESS, player, ticks, speedModifier);
    }

    private static void drawLasers(ItemStack stack, RenderWorldLastEvent event, Vector3d from, RayTraceResult trace, double xOffset, double yOffset, double zOffset, float thickness, PlayerEntity player, float ticks, float speedModifier) {
        Hand activeHand;
        if (player.getMainHandItem().getItem() instanceof ColorLaserGunItem) {
            activeHand = Hand.MAIN_HAND;
        } else if (player.getOffhandItem().getItem() instanceof ColorLaserGunItem) {
            activeHand = Hand.OFF_HAND;
        } else {
            return;
        }

        IVertexBuilder builder;
        double distance = Math.max(1, from.subtract(trace.getLocation()).length());
        long gameTime = player.level.getGameTime();
        double v = gameTime * speedModifier;
        float additiveThickness = (thickness * 3.5f) * calculateLaserFlickerModifier(gameTime);


        DyeColor color = ColorUtils.getColor(stack);
        float[] RGB = color.getTextureDiffuseColors();

        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        MatrixStack matrix = event.getMatrixStack();

        matrix.pushPose();

        matrix.translate(-view.x(), -view.y(), -view.z());
        matrix.translate(from.x, from.y, from.z);
        matrix.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(ticks, -player.yRot, -player.yRotO)));
        matrix.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(ticks, player.xRot, player.xRotO)));

        MatrixStack.Entry matrixstack$entry = matrix.last();
        Matrix3f matrixNormal = matrixstack$entry.normal();
        Matrix4f positionMatrix = matrixstack$entry.pose();

        //additive laser beam
        builder = buffer.getBuffer(ModRenderTypes.RENDERTYPE_LASER_ADDITIVE);
        drawBeam(stack, xOffset, yOffset, zOffset, builder, positionMatrix, matrixNormal, additiveThickness, activeHand, distance, 0.5, 1, ticks, RGB[0] * 0.8f, RGB[1] * 0.8f, RGB[2] * 0.8f,0.7f);

        //main laser, colored part
        builder = buffer.getBuffer(ModRenderTypes.RENDERTYPE_LASER_BEAM);
        drawBeam(stack, xOffset, yOffset, zOffset, builder, positionMatrix, matrixNormal, thickness, activeHand, distance, v, v + distance * 1.5, ticks, RGB[0] * 0.8f, RGB[1] * 0.8f, RGB[2] * 0.8f,1f);

        //core
        builder = buffer.getBuffer(ModRenderTypes.RENDERTYPE_LASER_CORE);
        drawBeam(stack, xOffset, yOffset, zOffset, builder, positionMatrix, matrixNormal, thickness/2, activeHand, distance, v, v + distance * 1.5, ticks, RGB[0], RGB[1], RGB[2],1f);
        matrix.popPose();
//        RenderSystem.disableDepthTest();
        buffer.endBatch();
    }

    private static float calculateLaserFlickerModifier(long gameTime) {
        return 0.9f + 0.1f * MathHelper.sin(gameTime * 0.99f) * MathHelper.sin(gameTime * 0.3f) * MathHelper.sin(gameTime * 0.1f);
    }

    private static void drawBeam(ItemStack stack, double xOffset, double yOffset, double zOffset, IVertexBuilder builder, Matrix4f positionMatrix, Matrix3f matrixNormalIn, float thickness, Hand hand, double distance, double v1, double v2, float ticks, float r, float g, float b, float alpha) {
        Vector3f vector3f = new Vector3f(0.0f, 1.0f, 0.0f);
        vector3f.transform(matrixNormalIn);
        ClientPlayerEntity player = Minecraft.getInstance().player;
        // Support for hand sides remembering to take into account of Skin options
        if( Minecraft.getInstance().options.mainHand != HandSide.RIGHT )
            hand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        float startXOffset = -0.25f;
        float startYOffset = -0.125f;
        float startZOffset = 1f;
        // Adjust for fov changing
        startZOffset += (1 - player.getFieldOfViewModifier());
        if (hand == Hand.OFF_HAND) {
            startXOffset = 0.25f;
        }
        float f = (MathHelper.lerp(ticks, player.xRotO, player.xRot) - MathHelper.lerp(ticks, player.xBobO, player.xBob));
        float f1 = (MathHelper.lerp(ticks, player.yRotO, player.yRot) - MathHelper.lerp(ticks, player.yBobO, player.yBob));
        startXOffset = startXOffset + (f1 / 750);
        startYOffset = startYOffset + (f / 750);

        Vector4f vec1 = new Vector4f(startXOffset, -thickness + startYOffset, startZOffset, 1.0F);
        vec1.transform(positionMatrix);
        Vector4f vec2 = new Vector4f((float) xOffset, -thickness + (float) yOffset, (float) distance + (float) zOffset, 1.0F);
        vec2.transform(positionMatrix);
        Vector4f vec3 = new Vector4f((float) xOffset, thickness + (float) yOffset, (float) distance + (float) zOffset, 1.0F);
        vec3.transform(positionMatrix);
        Vector4f vec4 = new Vector4f(startXOffset, thickness + startYOffset, startZOffset, 1.0F);
        vec4.transform(positionMatrix);

        if (hand == Hand.MAIN_HAND) {
            builder.vertex(vec4.x(), vec4.y(), vec4.z(), r, g, b, alpha, 0, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec3.x(), vec3.y(), vec3.z(), r, g, b, alpha, 0, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec2.x(), vec2.y(), vec2.z(), r, g, b, alpha, 1, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec1.x(), vec1.y(), vec1.z(), r, g, b, alpha, 1, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            //Rendering a 2nd time to allow you to see both sides in multiplayer, shouldn't be necessary with culling disabled but here we are....
            builder.vertex(vec1.x(), vec1.y(), vec1.z(), r, g, b, alpha, 1, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec2.x(), vec2.y(), vec2.z(), r, g, b, alpha, 1, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec3.x(), vec3.y(), vec3.z(), r, g, b, alpha, 0, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec4.x(), vec4.y(), vec4.z(), r, g, b, alpha, 0, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
        } else {
            builder.vertex(vec1.x(), vec1.y(), vec1.z(), r, g, b, alpha, 1, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec2.x(), vec2.y(), vec2.z(), r, g, b, alpha, 1, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec3.x(), vec3.y(), vec3.z(), r, g, b, alpha, 0, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec4.x(), vec4.y(), vec4.z(), r, g, b, alpha, 0, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            //Rendering a 2nd time to allow you to see both sides in multiplayer, shouldn't be necessary with culling disabled but here we are....
            builder.vertex(vec4.x(), vec4.y(), vec4.z(), r, g, b, alpha, 0, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec3.x(), vec3.y(), vec3.z(), r, g, b, alpha, 0, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec2.x(), vec2.y(), vec2.z(), r, g, b, alpha, 1, (float) v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
            builder.vertex(vec1.x(), vec1.y(), vec1.z(), r, g, b, alpha, 1, (float) v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
        }
    }

}
