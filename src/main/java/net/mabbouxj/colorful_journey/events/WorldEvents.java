package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.client.misc.LaserBeamRenderer;
import net.mabbouxj.colorful_journey.items.ColorLaserGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class WorldEvents {

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        List<AbstractClientPlayerEntity> players = Minecraft.getInstance().level.players();
        PlayerEntity myPlayer = Minecraft.getInstance().player;

        for (PlayerEntity player : players) {
            if (player.distanceToSqr(myPlayer) > 500)
                continue;

            if (player.isUsingItem() && player.getUseItem().getItem() instanceof ColorLaserGunItem) {
                LaserBeamRenderer.renderLaser(event, player, Minecraft.getInstance().getFrameTime());
            }
        }

    }

}
