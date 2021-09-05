package net.mabbouxj.colorful_journey.entities;

import com.google.common.collect.Maps;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.utils.ColorAttributeModifier;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.client.renderer.entity.model.PandaModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.Map;

import static net.mabbouxj.colorful_journey.ColorfulJourney.NBT_COLOR_ID;

public class ColoredPandaEntity extends PandaEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredPandaEntity.class, DataSerializers.INT);
    private final PandaModel<ColoredPandaEntity> ENTITY_MODEL = new PandaModel<>(9, 0.0F);
    private static final Map<Gene, ResourceLocation> LAYERS_LOCATION = Util.make(Maps.newEnumMap(PandaEntity.Gene.class), (variants) -> {
        variants.put(PandaEntity.Gene.NORMAL, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_panda_layer.png"));
        variants.put(PandaEntity.Gene.LAZY, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_lazy_panda_layer.png"));
        variants.put(PandaEntity.Gene.WORRIED, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_worried_panda_layer.png"));
        variants.put(PandaEntity.Gene.PLAYFUL, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_playful_panda_layer.png"));
        variants.put(PandaEntity.Gene.BROWN, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_brown_panda_layer.png"));
        variants.put(PandaEntity.Gene.WEAK, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_weak_panda_layer.png"));
        variants.put(PandaEntity.Gene.AGGRESSIVE, new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/panda/colored_aggressive_panda_layer.png"));
    });


    public ColoredPandaEntity(EntityType<? extends PandaEntity> type, World world) {
        super(type, world);
        this.setColor(ColorUtils.getRandomDyeColor());
    }

    public ColoredPandaEntity(World world, PandaEntity oldEntity, DyeColor color) {
        this(ModEntityTypes.COLORED_PANDA.get(color).get(), world);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setColor(color);
        MobUtils.initFromOldEntity(this, oldEntity);

    }

    public static AttributeModifierMap.MutableAttribute createAttributes(DyeColor color) {
        return MobEntity.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.15D * ColorAttributeModifier.SPEED.byColor(color))
                .add(Attributes.ATTACK_DAMAGE, 6.0D * ColorAttributeModifier.DAMAGE.byColor(color));
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
        return new ResourceLocation("minecraft", "entities/panda");
    }

    @Override
    public PandaModel<ColoredPandaEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYERS_LOCATION.getOrDefault(this.getVariant(), LAYERS_LOCATION.get(PandaEntity.Gene.NORMAL));
    }

}
