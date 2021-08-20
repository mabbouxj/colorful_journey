package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;

public class ModSounds {

    public static final RegistryObject<SoundEvent> COLOR_GUN_SHOOT = registerSound("color_gun_shoot");
    public static final RegistryObject<SoundEvent> COLOR_GUN_HANDLING = registerSound("color_gun_handling");
    public static final RegistryObject<SoundEvent> INK_SPLASH = registerSound("ink_splash");

    static void register() {
    }

    private static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
        return Registration.SOUND_EVENTS.register(name, () -> new SoundEvent(location));
    }
}
