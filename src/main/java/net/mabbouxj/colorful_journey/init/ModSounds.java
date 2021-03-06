package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<SoundEvent> COLOR_GUN_SHOOT = registerSound("color_gun_shoot");
    public static final RegistryObject<SoundEvent> COLOR_GUN_HANDLING = registerSound("color_gun_handling");
    public static final RegistryObject<SoundEvent> INK_SPLASH = registerSound("ink_splash");
    public static final RegistryObject<SoundEvent> LASER_START = registerSound("laser_start");
    public static final RegistryObject<SoundEvent> LASER_END = registerSound("laser_end");
    public static final RegistryObject<SoundEvent> LASER_LOOP = registerSound("laser_loop");
    public static final RegistryObject<SoundEvent> COLORFUL_PORTAL_AMBIENT = registerSound("colorful_portal_ambient");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation location = new ResourceLocation(ColorfulJourney.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> new SoundEvent(location));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
