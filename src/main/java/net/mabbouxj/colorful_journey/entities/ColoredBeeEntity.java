package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static net.mabbouxj.colorful_journey.Reference.NBT_COLOR_ID;


public class ColoredBeeEntity extends BeeEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredBeeEntity.class, DataSerializers.INT);
    private final BeeModel<ColoredBeeEntity> ENTITY_MODEL = new BeeModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(Reference.MOD_ID, "textures/entity/bee/colored_bee_layer.png");

    public ColoredBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredBeeEntity(World world, BeeEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_BEE.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        this.setRot(oldEntity.xRot, oldEntity.yRot);

        this.setAge(oldEntity.getAge());
        this.setHealth(oldEntity.getHealth());
        this.setAggressive(oldEntity.isAggressive());
        this.setBaby(oldEntity.isBaby());
        this.setStingerCount(oldEntity.getStingerCount());
        this.setAggressive(oldEntity.isAggressive());
        this.setPersistentAngerTarget(oldEntity.getPersistentAngerTarget());
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

    @Override
    public BeeModel<ColoredBeeEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

}
