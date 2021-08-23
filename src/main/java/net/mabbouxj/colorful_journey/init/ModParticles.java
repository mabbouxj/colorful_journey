package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ColorfulJourney.MOD_ID);

    public static final RegistryObject<ParticleType<InkSplashParticle.Data>> INK_SPLASH = PARTICLES.register("ink_splash", InkSplashParticle.Type::new);

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }

}
