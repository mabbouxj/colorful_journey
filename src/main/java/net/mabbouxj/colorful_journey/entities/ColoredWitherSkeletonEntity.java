package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static net.mabbouxj.colorful_journey.ColorfulJourney.NBT_COLOR_ID;

public class ColoredWitherSkeletonEntity extends WitherSkeletonEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredWitherSkeletonEntity.class, DataSerializers.INT);
    private final SkeletonModel<ColoredWitherSkeletonEntity> ENTITY_MODEL = new SkeletonModel<>(0.0F, false);
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/skeleton/colored_skeleton_layer.png");

    public ColoredWitherSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredWitherSkeletonEntity(World world, WitherSkeletonEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_WITHER_SKELETON.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        this.setYHeadRot(oldEntity.getYHeadRot());

        this.setHealth(oldEntity.getHealth());
        this.setAggressive(oldEntity.isAggressive());
        this.setArrowCount(oldEntity.getArrowCount());

        this.setItemSlot(EquipmentSlotType.HEAD, oldEntity.getItemBySlot(EquipmentSlotType.HEAD));
        this.setItemSlot(EquipmentSlotType.CHEST, oldEntity.getItemBySlot(EquipmentSlotType.CHEST));
        this.setItemSlot(EquipmentSlotType.LEGS, oldEntity.getItemBySlot(EquipmentSlotType.LEGS));
        this.setItemSlot(EquipmentSlotType.FEET, oldEntity.getItemBySlot(EquipmentSlotType.FEET));
        this.setItemSlot(EquipmentSlotType.MAINHAND, oldEntity.getItemBySlot(EquipmentSlotType.MAINHAND));
        this.setItemSlot(EquipmentSlotType.OFFHAND, oldEntity.getItemBySlot(EquipmentSlotType.OFFHAND));

        this.setItemInHand(Hand.MAIN_HAND, oldEntity.getMainHandItem());
        this.setItemInHand(Hand.OFF_HAND, oldEntity.getOffhandItem());

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(NBT_COLOR_ID, this.getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.setColor(DyeColor.byId(nbt.getInt(NBT_COLOR_ID)));
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR_ID));
    }

    public void setColor(DyeColor color) {
        this.entityData.set(DATA_COLOR_ID, color.getId());
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return new ResourceLocation("minecraft", "entities/wither_skeleton");
    }

    @Override
    public SkeletonModel<ColoredWitherSkeletonEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }
}
