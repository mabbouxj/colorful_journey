package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
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

public class ColoredEndermanEntity extends EndermanEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredEndermanEntity.class, DataSerializers.INT);
    private final EndermanModel<ColoredEndermanEntity> ENTITY_MODEL = new EndermanModel<>(0.0F);
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/enderman/colored_enderman_layer.png");

    public ColoredEndermanEntity(EntityType<? extends EndermanEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredEndermanEntity(World world, EndermanEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_ENDERMAN.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
        this.setYHeadRot(oldEntity.getYHeadRot());

        this.setHealth(oldEntity.getHealth());
        this.setAggressive(oldEntity.isAggressive());

        this.setCarriedBlock(oldEntity.getCarriedBlock());
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
        return new ResourceLocation("minecraft", "entities/enderman");
    }

    @Override
    public Item getReplacementItemFor(Item item, DyeColor color) {
        if (item == Items.ENDER_PEARL) {
            return ModItems.COLORED_ENDER_PEARL.get();
        }
        return null;
    }

    @Override
    public EndermanModel<ColoredEndermanEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }
}
