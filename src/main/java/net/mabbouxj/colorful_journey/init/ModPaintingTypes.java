package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPaintingTypes {

    private static DeferredRegister<PaintingType> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, ColorfulJourney.MOD_ID);

    public static RegistryObject<PaintingType> BOB_ROSS = PAINTING_TYPES.register("bob_ross",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> PEACEFUL_VALLEY = PAINTING_TYPES.register("peaceful_valley",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> TROPICAL_SEASCAPE = PAINTING_TYPES.register("tropical_seascape",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> SUNSET_AGLOW = PAINTING_TYPES.register("sunset_aglow",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> MOUNTAIN_SUMMIT = PAINTING_TYPES.register("mountain_summit",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> OCEAN_SUNRISE = PAINTING_TYPES.register("ocean_sunrise",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> NORTHERN_LIGHTS = PAINTING_TYPES.register("northern_lights",()-> new PaintingType(32,32));
    public static RegistryObject<PaintingType> EVENINGS_GLOW = PAINTING_TYPES.register("evenings_glow",()-> new PaintingType(32,32));

    public static void register(IEventBus bus) {
        PAINTING_TYPES.register(bus);
    }

}
