package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class ColoredSkeletonEntity extends SkeletonEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredSkeletonEntity.class, DataSerializers.INT);

    public ColoredSkeletonEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static ColoredSkeletonEntity newFromExistingEntity(SkeletonEntity oldEntity, World world, DyeColor color) {
        ColoredSkeletonEntity newEntity = new ColoredSkeletonEntity(ModEntities.COLORED_SKELETON.get(), world);
        newEntity.setColor(color);

        newEntity.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());

        newEntity.setHealth(oldEntity.getHealth());
        newEntity.setAggressive(oldEntity.isAggressive());
        newEntity.setArrowCount(oldEntity.getArrowCount());
        newEntity.setItemInHand(newEntity.getUsedItemHand(), oldEntity.getMainHandItem());

        return newEntity;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Color", this.getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.setColor(DyeColor.byId(nbt.getInt("Color")));
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

    public ColorfulItem getReplacementItemFor(Item item) {
        if (item == Items.BONE) {
            return (ColorfulItem) ModItems.COLORED_BONE.get();
        }
        return null;
    }
}
