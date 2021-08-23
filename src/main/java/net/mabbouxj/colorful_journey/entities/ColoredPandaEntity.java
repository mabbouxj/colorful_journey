package net.mabbouxj.colorful_journey.entities;

import com.google.common.collect.Maps;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModEntities;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.renderer.entity.model.PandaModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
        this(ModEntities.COLORED_PANDA.get(), world);
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
        return new ResourceLocation("minecraft", "entities/panda");
    }

    public ColorfulItem getReplacementItemFor(Item item) {
        if (item == Items.BAMBOO) {
            return (ColorfulItem) ModItems.COLORED_BAMBOO.get();
        }
        return null;
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
