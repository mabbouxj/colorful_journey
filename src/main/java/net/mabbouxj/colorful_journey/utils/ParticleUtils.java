package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class ParticleUtils {

    public static void makeParticles(World world, DyeColor color, double speed, int amount, Vector3d position) {
        final double TICKS_PER_SECOND = 20;
        final double SPEED_IN_BLOCKS_PER_TICK = speed / TICKS_PER_SECOND;
        for (int i = 0; i < amount; i++) {
            Vector3d direction = new Vector3d(
                    2 * new Random().nextDouble() - 1,
                    2 * new Random().nextDouble() - 1,
                    2 * new Random().nextDouble() - 1
            );
            double velocityX = SPEED_IN_BLOCKS_PER_TICK * direction.x;
            double velocityY = SPEED_IN_BLOCKS_PER_TICK * direction.y;
            double velocityZ = SPEED_IN_BLOCKS_PER_TICK * direction.z;

            if (world instanceof ServerWorld) {
                ((ServerWorld) world).sendParticles(new InkSplashParticle.Data(color), position.x, position.y, position.z, 1, velocityX, velocityY, velocityZ, 0.15);
            } else {
                world.addParticle(new InkSplashParticle.Data(color), position.x, position.y, position.z, velocityX, velocityY, velocityZ);
            }
        }
    }

}
