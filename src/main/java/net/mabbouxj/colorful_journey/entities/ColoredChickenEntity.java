package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.enums.ColorAttributesModifier;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class ColoredChickenEntity extends ChickenEntity implements IColoredMobEntity {

    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.defineId(ColoredChickenEntity.class, DataSerializers.INT);
    private final ChickenModel<ColoredChickenEntity> ENTITY_MODEL = new ChickenModel<>();
    private final ResourceLocation LAYER_LOCATION = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/entity/chicken/colored_chicken_layer.png");

    public ColoredChickenEntity(EntityType<? extends ChickenEntity> type, World world) {
        super(type, world);
        this.setColor(ColorUtils.getRandomEnableColor());
    }

    public ColoredChickenEntity(World world, ChickenEntity oldEntity, DyeColor color) {
        this(ModEntityTypes.COLORED_CHICKEN.get(color).get(), world);

        if (oldEntity.getEntityData().getAll() == null) {
            this.entityData.assignValues(oldEntity.getEntityData().getAll());
        }

        this.setColor(color);
        MobUtils.initFromOldEntity(this, oldEntity);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(DyeColor color) {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D * ColorAttributesModifier.HEALTH.byColor(color))
                .add(Attributes.MOVEMENT_SPEED, 0.25D * ColorAttributesModifier.SPEED.byColor(color));
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

    public void aiStep() {
        // Catch the moment when the vanilla chicken lays its egg and replace it by a colored one
        // This resets the eggTime so the super.aiStep() will not spawn it again
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            RegistryObject<? extends Item> coloredEgg = ModItems.COLORED_EGGS.getOrDefault(this.getColor(), null);
            if (coloredEgg != null) {
                ItemStack coloredEggStack = new ItemStack(coloredEgg.get(), 1);
                this.spawnAtLocation(coloredEggStack);
                this.eggTime = this.random.nextInt(6000) + 6000;
            }
        }
        super.aiStep();
    }

    @Override
    public ChickenModel<ColoredChickenEntity> getEntityModel() {
        return ENTITY_MODEL;
    }

    @Override
    public ResourceLocation getLayerLocation() {
        return LAYER_LOCATION;
    }

}
