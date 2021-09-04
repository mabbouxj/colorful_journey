package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ModEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ColorfulJourney.MOD_ID);

    public static final RegistryObject<EntityType<InkBallEntity>> INK_BALL = registerMiscEntity("ink_ball", InkBallEntity::new);

    public static final Map<DyeColor, RegistryObject<EntityType<ColoredChickenEntity>>> COLORED_CHICKEN = registerColoredMob("colored_chicken", ColoredChickenEntity::new, EntityType.CHICKEN);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredBeeEntity>>> COLORED_BEE = registerColoredMob("colored_bee", ColoredBeeEntity::new, EntityType.BEE);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredCowEntity>>> COLORED_COW = registerColoredMob("colored_cow", ColoredCowEntity::new, EntityType.COW);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredPandaEntity>>> COLORED_PANDA = registerColoredMob("colored_panda", ColoredPandaEntity::new, EntityType.PANDA);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredSkeletonEntity>>> COLORED_SKELETON = registerColoredMob("colored_skeleton", ColoredSkeletonEntity::new, EntityType.SKELETON);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredZombieEntity>>> COLORED_ZOMBIE = registerColoredMob("colored_zombie", ColoredZombieEntity::new, EntityType.ZOMBIE);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredSpiderEntity>>> COLORED_SPIDER = registerColoredMob("colored_spider", ColoredSpiderEntity::new, EntityType.SPIDER);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredEndermanEntity>>> COLORED_ENDERMAN = registerColoredMob("colored_enderman", ColoredEndermanEntity::new, EntityType.ENDERMAN);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredWitherSkeletonEntity>>> COLORED_WITHER_SKELETON = registerColoredMob("colored_wither_skeleton", ColoredWitherSkeletonEntity::new, EntityType.WITHER_SKELETON);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredWitherEntity>>> COLORED_WITHER = registerColoredMob("colored_wither", ColoredWitherEntity::new, EntityType.WITHER);
    public static final Map<DyeColor, RegistryObject<EntityType<ColoredCreeperEntity>>> COLORED_CREEPER = registerColoredMob("colored_creeper", ColoredCreeperEntity::new, EntityType.CREEPER);

    private static <T extends Entity> Map<DyeColor, RegistryObject<EntityType<T>>> registerColoredMob(String name, EntityType.IFactory<T> factory, EntityType<?> fromEntity) {
        Map<DyeColor, RegistryObject<EntityType<T>>> map = new HashMap<>();
        for (DyeColor color: ColorfulJourney.COLORS) {
            EntityType<T> entityType = EntityType.Builder
                    .of(factory, fromEntity.getCategory())
                    .sized(fromEntity.getWidth(), fromEntity.getHeight())
                    .setTrackingRange(fromEntity.clientTrackingRange())
                    .build(new ResourceLocation(ColorfulJourney.MOD_ID, name).toString());
            RegistryObject<EntityType<T>> entityTypeRegistryObject = ENTITY_TYPES.register(name + "_" + color.getName(), () -> entityType);
            map.put(color, entityTypeRegistryObject);
        }
        return map;
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerMiscEntity(String id, BiFunction<EntityType<T>, World, T> function) {
        EntityType<T> type = EntityType.Builder.of(function::apply, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .build(id);
        return ENTITY_TYPES.register(id, () -> type);
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
