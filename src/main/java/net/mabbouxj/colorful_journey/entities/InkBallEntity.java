package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
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
import java.util.logging.Logger;

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

    public InkBallEntity(World world, double x, double y, double z) {
        super(ModEntities.INK_BALL.get(), x, y, z, world);
        this.color = ColorUtils.getRandomDyeColor();
    }

    public DyeColor getColor() {
        return this.color;
    }

    // If you forget to override this method, the default vanilla method will be called.
    // This sends a vanilla spawn packet, which is then silently discarded when it reaches the client.
    //  Your entity will be present on the server and can cause effects, but the client will not have a copy of the entity
    //    and hence it will not render.
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // ProjectileItemEntity::setItem uses this to save storage space.  It only stores the itemStack if the itemStack is not
    //   the default item.
    @Override
    protected Item getDefaultItem() {
        return ModItems.INK_BALL.get().asItem();
    }

    // We hit something (entity or block).
    @Override
    protected void onHit(RayTraceResult rayTraceResult) {

        if (!this.level.isClientSide()) {

            if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
                Entity entity = entityRayTraceResult.getEntity();

                Logger.getLogger("").warning("InkBall color: " + this.getColor().getName());
                this.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.INK_SPLASH.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);

                if (entity instanceof IColoredMobEntity) {
                    ((IColoredMobEntity) entity).setColor(this.getColor());
                } else if (entity instanceof SheepEntity) {
                    SheepEntity oldEntity = (SheepEntity) entity;
                    oldEntity.setColor(this.getColor());
                } else if (entity instanceof ChickenEntity) {
                    ChickenEntity oldEntity = (ChickenEntity) entity;
                    ColoredChickenEntity newEntity = ColoredChickenEntity.newFromExistingEntity(oldEntity, this.level, this.getColor());
                    oldEntity.remove();
                    this.level.addFreshEntity(newEntity);
                }
            } else if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                Logger.getLogger("").warning("InkBall color: " + this.getColor().getName());
            }

            this.level.broadcastEntityEvent(this, VANILLA_IMPACT_STATUS_ID);
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