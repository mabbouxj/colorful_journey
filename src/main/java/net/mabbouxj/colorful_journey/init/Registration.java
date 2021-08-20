package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.client.entity.render.ColoredChickenRenderer;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.events.ColoredMobEvent;
import net.mabbouxj.colorful_journey.utils.CustomItemColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;


public class Registration {

    public static final DeferredRegister<Item> ITEMS = createDeferredRegister(ForgeRegistries.ITEMS);
    public static final DeferredRegister<EntityType<?>> ENTITIES = createDeferredRegister(ForgeRegistries.ENTITIES);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = createDeferredRegister(ForgeRegistries.SOUND_EVENTS);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = createDeferredRegister(ForgeRegistries.PARTICLE_TYPES);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = createDeferredRegister(ForgeRegistries.RECIPE_SERIALIZERS);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(Common::entityAttributeCreationEvent);
        modEventBus.addListener(Client::onClientSetup);

        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        ENTITIES.register(modEventBus);
        PARTICLES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);

        ModItems.register();
        ModEntities.register();
        ModSounds.register();
        ModParticles.register();
        ModRecipeSerializers.register();

        MinecraftForge.EVENT_BUS.register(new ColoredMobEvent());

    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createDeferredRegister(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, Reference.MOD_ID);
    }

    @Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Common {
        private Common() {
        }

        @SubscribeEvent
        public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntities.COLORED_CHICKEN.get(), ChickenEntity.createAttributes().build());
        }

    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Client {
        private Client() {
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            ModelLoader.addSpecialModel(InkBallRenderer.MODEL_LOCATION);

            //RenderingRegistry.registerEntityRenderingHandler(ModEntities.INK_BALL.get(), InkBallRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.INK_BALL.get(), erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));

            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_CHICKEN.get(), ColoredChickenRenderer::new);
        }

        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event) {
            event.getItemColors().register(new CustomItemColor(),
                    ModItems.COLORED_FEATHER.get(),
                    ModItems.COLORED_EGG.get()
            );
        }

        @SubscribeEvent
        public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particleEngine.register(ModParticles.INK_SPLASH.get(), InkSplashParticle.Factory::new);
        }

    }

}
