package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
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

public class ColoredZombieEntity extends ZombieEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredZombieEntity.class, DataSerializers.INT);
    private final ZombieModel<ColoredZombieEntity> ENTITY_MODEL = new ZombieModel<>(0.0F, false);
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/zombie/colored_zombie_layer.png");

    public ColoredZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredZombieEntity(World world, ZombieEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_ZOMBIE.get(), world);
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
        return new ResourceLocation("minecraft", "entities/zombie");
    }

    public ColorfulItem getReplacementItemFor(Item item) {
        if (item == Items.ROTTEN_FLESH) {
            return (ColorfulItem) ModItems.COLORED_ROTTEN_FLESH.get();
        }
        return null;
    }

    @Override
    public ZombieModel<ColoredZombieEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }
}