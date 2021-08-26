package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static net.mabbouxj.colorful_journey.ColorfulJourney.NBT_COLOR_ID;


public class ColoredSkeletonEntity extends SkeletonEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredSkeletonEntity.class, DataSerializers.INT);
    private final SkeletonModel<ColoredSkeletonEntity> ENTITY_MODEL = new SkeletonModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/skeleton/colored_skeleton_layer.png");

    public ColoredSkeletonEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredSkeletonEntity(World world, SkeletonEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_SKELETON.get(), world);
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
        return new ResourceLocation("minecraft", "entities/skeleton");
    }

    @Override
    public Item getReplacementItemFor(Item item, DyeColor color) {
        if (item == Items.BONE) {
            return ModItems.COLORED_BONE.get();
        } else if (item == Items.SKELETON_SKULL) {
            return ModItems.COLORED_SKULL.get();
        }
        return null;
    }

    @Override
    public SkeletonModel<ColoredSkeletonEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }
}
