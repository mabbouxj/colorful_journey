package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.enums.ColorAttributesModifier;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ColoredBlazeEntity extends BlazeEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredBlazeEntity.class, DataSerializers.INT);
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/blaze/colored_blaze_layer.png");

    public ColoredBlazeEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
        this.setColor(ColorUtils.getRandomEnabledColor());
    }

    public ColoredBlazeEntity(World world, BlazeEntity oldEntity, DyeColor color) {
        this(ModEntityTypes.COLORED_BLAZE.get(color).get(), world);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setColor(color);
        MobUtils.initFromOldEntity(this, oldEntity);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(DyeColor color) {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D * ColorAttributesModifier.DAMAGE.byColor(color))
                .add(Attributes.MOVEMENT_SPEED, 0.23F * ColorAttributesModifier.SPEED.byColor(color))
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
    public ResourceLocation getDefaultLootTable() {
        return new ResourceLocation("minecraft", "entities/blaze");
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
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

}
