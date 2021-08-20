package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;

public class ModParticles {

    public static final RegistryObject<ParticleType<InkSplashParticle.Data>> INK_SPLASH = Registration.PARTICLES.register("ink_splash", InkSplashParticle.Type::new);

    static void register() {
    }

}
