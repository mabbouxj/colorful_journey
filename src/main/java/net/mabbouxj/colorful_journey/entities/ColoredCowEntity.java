package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
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

public class ColoredCowEntity extends CowEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredCowEntity.class, DataSerializers.INT);
    private final CowModel<ColoredCowEntity> ENTITY_MODEL = new CowModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/cow/colored_cow_layer.png");

    public ColoredCowEntity(EntityType<? extends CowEntity> type, World world) {
        super(type, world);
        this.setColor(ColorUtils.getRandomDyeColor());
    }

    public ColoredCowEntity(World world, CowEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_COW.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        this.setYHeadRot(oldEntity.getYHeadRot());

        this.setAge(oldEntity.getAge());
        this.setHealth(oldEntity.getHealth());
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
        return new ResourceLocation("minecraft", "entities/cow");
    }

    @Override
    public Item getReplacementItemFor(Item item, DyeColor color) {
        if (item == Items.LEATHER) {
            return ModItems.COLORED_LEATHER.get();
        }
        return null;
    }

    @Override
    public CowModel<ColoredCowEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

}
