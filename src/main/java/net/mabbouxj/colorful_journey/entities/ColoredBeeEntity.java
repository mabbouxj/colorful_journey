package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;


public class ColoredBeeEntity extends BeeEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredBeeEntity.class, DataSerializers.INT);

    public ColoredBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }

    public static ColoredBeeEntity newFromExistingEntity(BeeEntity oldEntity, World world, DyeColor color) {
        ColoredBeeEntity newEntity = new ColoredBeeEntity(ModEntities.COLORED_BEE.get(), world);
        newEntity.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            newEntity.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        newEntity.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        newEntity.setRot(oldEntity.xRot, oldEntity.yRot);

        newEntity.setAge(oldEntity.getAge());
        newEntity.setHealth(oldEntity.getHealth());
        newEntity.setAggressive(oldEntity.isAggressive());
        newEntity.setBaby(oldEntity.isBaby());
        newEntity.setStingerCount(oldEntity.getStingerCount());
        newEntity.setAggressive(oldEntity.isAggressive());
        newEntity.setPersistentAngerTarget(oldEntity.getPersistentAngerTarget());

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

    @Override
    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR_ID));
    }

    @Override
    public void setColor(DyeColor color) {
        this.entityData.set(DATA_COLOR_ID, color.getId());
    }

    @Override
    public ColorfulItem getReplacementItemFor(Item item) {
        if (item == Items.HONEYCOMB) {
            return (ColorfulItem) ModItems.COLORED_HONEYCOMB.get();
        }
        return null;
    }

}
