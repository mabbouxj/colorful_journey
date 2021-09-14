package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.mabbouxj.colorful_journey.utils.ParticleUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Map;

public class InkBallEntity extends ProjectileItemEntity implements IEntityAdditionalSpawnData {

    private DyeColor color = ColorUtils.getRandomEnabledColor();
    private LivingEntity shooter;

    public InkBallEntity(EntityType<? extends InkBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public InkBallEntity(World world, LivingEntity livingEntity) {
        super(ModEntityTypes.INK_BALL.get(), livingEntity, world);
        this.shooter = livingEntity;
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
            ParticleUtils.makeParticles(this.level, this.getColor(), 8, 40, new Vector3d(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D)));

            if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
                Entity entity = entityRayTraceResult.getEntity();
                Entity newEntity = MobUtils.getNewColoredEntityFrom(this.shooter, entity, this.getColor());
                if (newEntity != null) {
                    entity.remove();
                    this.level.addFreshEntity(newEntity);
                }
            } else if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockState oldBlockState = this.level.getBlockState(blockRayTraceResult.getBlockPos());
                Map<DyeColor, RegistryObject<? extends Block>> targets = ColorfulJourney.REPLACEMENT_BLOCKS.getOrDefault(oldBlockState.getBlock(), null);
                if (targets != null) {
                    try {
                        Block newBlock = targets.get(this.getColor()).get();
                        this.level.setBlock(blockRayTraceResult.getBlockPos(), newBlock.defaultBlockState(), 3);
                    } catch (Exception e) {
                        ColorfulJourney.LOGGER.info("Could not create replacement block for " + oldBlockState.getBlock().getName());
                    }
                }
            }
            this.remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ParticleUtils.makeParticles(this.level, this.getColor(), 2, 3, new Vector3d(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D)));
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