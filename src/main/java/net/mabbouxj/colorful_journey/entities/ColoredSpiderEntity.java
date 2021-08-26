package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static net.mabbouxj.colorful_journey.ColorfulJourney.NBT_COLOR_ID;

public class ColoredSpiderEntity extends SpiderEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredSpiderEntity.class, DataSerializers.INT);
    private final SpiderModel<ColoredSpiderEntity> ENTITY_MODEL = new SpiderModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/spider/colored_spider_layer.png");

    public ColoredSpiderEntity(EntityType<? extends SpiderEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredSpiderEntity(World world, SpiderEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_SPIDER.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        this.setYHeadRot(oldEntity.getYHeadRot());

        this.setHealth(oldEntity.getHealth());
        this.setAggressive(oldEntity.isAggressive());
        this.setArrowCount(oldEntity.getArrowCount());

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
        return new ResourceLocation("minecraft", "entities/spider");
    }

    @Override
    public Item getReplacementItemFor(Item item, DyeColor color) {
        if (item == Items.STRING) {
            return ModItems.COLORED_STRING.get();
        }
        return null;
    }

    @Override
    public SpiderModel<ColoredSpiderEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }
}
