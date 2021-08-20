package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;


public class ColoredChickenEntity extends ChickenEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredChickenEntity.class, DataSerializers.INT);

    public ColoredChickenEntity(EntityType<? extends ChickenEntity> type, World world) {
        super(type, world);
        this.setColor(ColorUtils.getRandomDyeColor());
    }

    public static ColoredChickenEntity newFromExistingEntity(ChickenEntity oldEntity, World world, DyeColor color) {
        ColoredChickenEntity newEntity = new ColoredChickenEntity(ModEntities.COLORED_CHICKEN.get(), world);
        newEntity.setColor(color);

        newEntity.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        newEntity.setRot(oldEntity.xRot, oldEntity.yRot);

        newEntity.setAge(oldEntity.getAge());
        newEntity.setHealth(oldEntity.getHealth());

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
        return new ResourceLocation("minecraft", "entities/chicken");
    }

    public ColorfulItem getReplacementItemFor(Item item) {
        if (item == Items.FEATHER) {
            return (ColorfulItem) ModItems.COLORED_FEATHER.get();
        } else if (item == Items.EGG) {
            return (ColorfulItem) ModItems.COLORED_EGG.get();
        }
        return null;
    }

    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            ItemStack coloredEggStack = new ItemStack(ModItems.COLORED_EGG.get(), 1);
            ColorfulItem.setColor(coloredEggStack, this.getColor());

            this.spawnAtLocation(coloredEggStack);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
        super.aiStep();
    }

}
