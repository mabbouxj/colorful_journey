package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.enums.ColorAttributesModifier;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ColoredBeeEntity extends BeeEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredBeeEntity.class, DataSerializers.INT);
    private final BeeModel<ColoredBeeEntity> ENTITY_MODEL = new BeeModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/bee/colored_bee_layer.png");

    public ColoredBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
        this.setColor(ColorUtils.getRandomEnableColor());
    }

    public ColoredBeeEntity(World world, BeeEntity oldEntity, DyeColor color) {
        this(ModEntityTypes.COLORED_BEE.get(color).get(), world);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setColor(color);
        MobUtils.initFromOldEntity(this, oldEntity);
        this.setStingerCount(oldEntity.getStingerCount());
        this.setPersistentAngerTarget(oldEntity.getPersistentAngerTarget());
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(DyeColor color) {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D * ColorAttributesModifier.HEALTH.byColor(color))
                .add(Attributes.FLYING_SPEED, 0.6D * ColorAttributesModifier.SPEED.byColor(color))
                .add(Attributes.MOVEMENT_SPEED, 0.3D * ColorAttributesModifier.SPEED.byColor(color))
                .add(Attributes.ATTACK_DAMAGE, 2.0D * ColorAttributesModifier.DAMAGE.byColor(color))
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(ColorUtils.NBT_TAG_COLOR, this.getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.setColor(DyeColor.byId(nbt.getInt(ColorUtils.NBT_TAG_COLOR)));
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
    public BeeModel<ColoredBeeEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

}
