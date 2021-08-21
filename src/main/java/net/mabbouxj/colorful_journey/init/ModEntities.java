package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.entities.ColoredBeeEntity;
import net.mabbouxj.colorful_journey.entities.ColoredChickenEntity;
import net.mabbouxj.colorful_journey.entities.InkBallEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.BiFunction;

public class ModEntities {

    public static final RegistryObject<EntityType<InkBallEntity>> INK_BALL = registerMiscEntity("ink_ball", InkBallEntity::new);
    public static final RegistryObject<EntityType<ColoredChickenEntity>> COLORED_CHICKEN = registerCreatureEntity("colored_chicken", ColoredChickenEntity::new);
    public static final RegistryObject<EntityType<ColoredBeeEntity>> COLORED_BEE = registerCreatureEntity("colored_bee", ColoredBeeEntity::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerCreatureEntity(String id, BiFunction<EntityType<T>, World, T> function) {
        EntityType<T> type = EntityType.Builder.of(function::apply, EntityClassification.CREATURE)
                .sized(1.0F, 1.0F)
                .build(new ResourceLocation(Reference.MOD_ID, id).toString());
        return Registration.ENTITIES.register(id, () -> type);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerMiscEntity(String id, BiFunction<EntityType<T>, World, T> function) {
        EntityType<T> type = EntityType.Builder.of(function::apply, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .build(id);
        return Registration.ENTITIES.register(id, () -> type);
    }

    static void register() {
    }
}
