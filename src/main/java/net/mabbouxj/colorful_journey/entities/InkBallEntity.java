package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.awt.*;
import java.util.Random;

public class InkBallEntity extends ProjectileItemEntity implements IEntityAdditionalSpawnData {

    private static final byte VANILLA_IMPACT_STATUS_ID = 3;
    private DyeColor color;

    public InkBallEntity(EntityType<? extends InkBallEntity> entityType, World world) {
        super(entityType, world);
        this.color = ColorUtils.getRandomDyeColor();
    }

    public InkBallEntity(World world, LivingEntity livingEntity) {
        super(ModEntities.INK_BALL.get(), livingEntity, world);
        this.color = ColorUtils.getRandomDyeColor();
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.INK_BALL.get().asItem();
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {

        if (!this.level.isClientSide()) {

            Vector3d hitLocation = rayTraceResult.getLocation();
            this.level.playSound(null, hitLocation.x, hitLocation.y, hitLocation.z, ModSounds.INK_SPLASH.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.level.broadcastEntityEvent(this, VANILLA_IMPACT_STATUS_ID);

            if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
                Entity entity = entityRayTraceResult.getEntity();
                Entity newEntity = null;

                if (entity instanceof IColoredMobEntity) {
                    ((IColoredMobEntity) entity).setColor(this.getColor());
                } else if (entity instanceof SheepEntity) {
                    ((SheepEntity) entity).setColor(this.getColor());
                } else if (entity instanceof ChickenEntity) {
                    newEntity = new ColoredChickenEntity(this.level, (ChickenEntity) entity, this.getColor());
                } else if (entity instanceof BeeEntity) {
                    newEntity = new ColoredBeeEntity(this.level, (BeeEntity) entity, this.getColor());
                } else if (entity instanceof SkeletonEntity) {
                    newEntity = new ColoredSkeletonEntity(this.level, (SkeletonEntity) entity, this.getColor());
                } else if (entity instanceof CowEntity) {
                    newEntity = new ColoredCowEntity(this.level, (CowEntity) entity, this.getColor());
                }

                if (newEntity != null) {
                    entity.remove();
                    this.level.addFreshEntity(newEntity);
                }
            }
            this.remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void handleEntityEvent(byte statusID) {
        if (statusID == VANILLA_IMPACT_STATUS_ID) {

            final double SPEED_IN_BLOCKS_PER_SECOND = 5.0;
            final double TICKS_PER_SECOND = 20;
            final double SPEED_IN_BLOCKS_PER_TICK = SPEED_IN_BLOCKS_PER_SECOND / TICKS_PER_SECOND;

            Color color = new Color(this.getColor().getColorValue());

            for (int i = 0; i < 40; i++) {
                Vector3d direction = new Vector3d(
                        2 * new Random().nextDouble() - 1,
                        2 * new Random().nextDouble() - 1,
                        2 * new Random().nextDouble() - 1
                );

                double velocityX = SPEED_IN_BLOCKS_PER_TICK * direction.x;
                double velocityY = SPEED_IN_BLOCKS_PER_TICK * direction.y;
                double velocityZ = SPEED_IN_BLOCKS_PER_TICK * direction.z;

                double diameter = new Random().nextDouble();

                InkSplashParticle.Data inkSplashParticleData = new InkSplashParticle.Data(color, diameter);
                this.level.addParticle(inkSplashParticleData, this.getX(), this.getY(), this.getZ(), velocityX, velocityY, velocityZ);
            }

        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(color.getId());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        color = DyeColor.byId(additionalData.readInt());
    }
}