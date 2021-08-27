package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ColorfulJourney.MOD_ID);

    public static final RegistryObject<EntityType<InkBallEntity>> INK_BALL = registerMiscEntity("ink_ball", InkBallEntity::new);
    public static final RegistryObject<EntityType<ColoredChickenEntity>> COLORED_CHICKEN = registerColoredMob("colored_chicken", ColoredChickenEntity::new);
    public static final RegistryObject<EntityType<ColoredBeeEntity>> COLORED_BEE = registerColoredMob("colored_bee", ColoredBeeEntity::new);
    public static final RegistryObject<EntityType<ColoredCowEntity>> COLORED_COW = registerColoredMob("colored_cow", ColoredCowEntity::new);
    public static final RegistryObject<EntityType<ColoredPandaEntity>> COLORED_PANDA = registerColoredMob("colored_panda", ColoredPandaEntity::new);
    public static final RegistryObject<EntityType<ColoredSkeletonEntity>> COLORED_SKELETON = registerColoredMob("colored_skeleton", ColoredSkeletonEntity::new);
    public static final RegistryObject<EntityType<ColoredZombieEntity>> COLORED_ZOMBIE = registerColoredMob("colored_zombie", ColoredZombieEntity::new);
    public static final RegistryObject<EntityType<ColoredSpiderEntity>> COLORED_SPIDER = registerColoredMob("colored_spider", ColoredSpiderEntity::new);
    public static final RegistryObject<EntityType<ColoredEndermanEntity>> COLORED_ENDERMAN = registerColoredMob("colored_enderman", ColoredEndermanEntity::new);
    public static final RegistryObject<EntityType<ColoredWitherSkeletonEntity>> COLORED_WITHER_SKELETON = registerColoredMob("colored_wither_skeleton", ColoredWitherSkeletonEntity::new);
    public static final RegistryObject<EntityType<ColoredWitherEntity>> COLORED_WITHER = registerColoredMob("colored_wither", ColoredWitherEntity::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerColoredMob(String id, BiFunction<EntityType<T>, World, T> function) {
        EntityType<T> type = EntityType.Builder.of(function::apply, EntityClassification.CREATURE)
                .sized(1.0F, 1.0F)
                .build(new ResourceLocation(ColorfulJourney.MOD_ID, id).toString());
        return ENTITIES.register(id, () -> type);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerMiscEntity(String id, BiFunction<EntityType<T>, World, T> function) {
        EntityType<T> type = EntityType.Builder.of(function::apply, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .build(id);
        return ENTITIES.register(id, () -> type);
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
