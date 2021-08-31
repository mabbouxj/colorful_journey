package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.client.renderer.entity.model.WitherModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import static net.mabbouxj.colorful_journey.ColorfulJourney.NBT_COLOR_ID;

public class ColoredWitherEntity extends WitherEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredWitherEntity.class, DataSerializers.INT);
    private final WitherModel<ColoredWitherEntity> ENTITY_MODEL = new WitherModel<>(0.0F);
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/wither/colored_wither_layer.png");

    public ColoredWitherEntity(EntityType<? extends WitherEntity> entityType, World world) {
        super(entityType, world);
    }

    public ColoredWitherEntity(World world, WitherEntity oldEntity, DyeColor color) {
        this(ModEntities.COLORED_WITHER.get(), world);
        this.setColor(color);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        MobUtils.initFromOldEntity(this, oldEntity);

        this.setInvulnerable(oldEntity.isInvulnerable());
        this.setInvulnerableTicks(oldEntity.getInvulnerableTicks());
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
        this.setCustomName(getName());
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return new ResourceLocation("minecraft", "entities/wither");
    }

    @Override
    public WitherModel<ColoredWitherEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

    @Override
    public ITextComponent getName() {
        String colorName = StringUtils.capitalize(this.getColor().getName());
        String itemName = new TranslationTextComponent("entity.colorful_journey.colored_wither").getString();
        return new StringTextComponent(colorName + " " + itemName);
    }
}
