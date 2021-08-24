package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.client.entity.render.*;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.events.MobEvent;
import net.mabbouxj.colorful_journey.init.*;
import net.mabbouxj.colorful_journey.utils.ColorfulItemColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static net.mabbouxj.colorful_journey.ColorfulJourney.MOD_ID;


@Mod(MOD_ID)
public class ColorfulJourney {

    public static final String MOD_ID = "colorful_journey";
    public static final String MOD_NAME = "Colorful Journey";
    public static final String MOD_VERSION = "0.0.1";
    public static final ItemGroup MOD_ITEM_GROUP = new ColorfulJourneyItemGroup();
    public static final String NBT_COLOR_ID = "color";

    public static final DyeColor[] COLORS = new DyeColor[]{
            DyeColor.RED,
            DyeColor.GREEN,
            DyeColor.BLUE,
            DyeColor.CYAN,
            DyeColor.ORANGE,
            DyeColor.PINK
    };

    public ColorfulJourney() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.register(this);
        ModItems.register(bus);
        ModEntities.register(bus);
        ModSounds.register(bus);
        ModParticles.register(bus);
        ModRecipeSerializers.register(bus);

        bus.addListener(Common::onCommonSetup);
        bus.addListener(Common::onEntityAttributeCreationEvent);
        bus.addListener(Client::onClientSetup);
        bus.addListener(Client::onColorHandlerEvent);
        bus.addListener(Client::onParticleFactoryRegistration);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Common {
        private Common() {
        }

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(new MobEvent());
        }

        @SubscribeEvent
        public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntities.COLORED_CHICKEN.get(), ChickenEntity.createAttributes().build());
            event.put(ModEntities.COLORED_BEE.get(), BeeEntity.createAttributes().build());
            event.put(ModEntities.COLORED_SKELETON.get(), SkeletonEntity.createAttributes().build());
            event.put(ModEntities.COLORED_COW.get(), CowEntity.createAttributes().build());
            event.put(ModEntities.COLORED_PANDA.get(), PandaEntity.createAttributes().build());
            event.put(ModEntities.COLORED_ZOMBIE.get(), ZombieEntity.createAttributes().build());
        }

    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Client {
        private Client() {
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.INK_BALL.get(), erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_CHICKEN.get(), ColoredChickenRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_BEE.get(), ColoredBeeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_SKELETON.get(), ColoredSkeletonRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_COW.get(), ColoredCowRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_PANDA.get(), ColoredPandaRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_ZOMBIE.get(), ColoredZombieRenderer::new);
        }

        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event) {
            for (RegistryObject<Item> registryItem : ModItems.COLORFUL_ITEMS) {
                event.getItemColors().register(new ColorfulItemColor(), registryItem.get());
            }
        }

        @SubscribeEvent
        public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particleEngine.register(ModParticles.INK_SPLASH.get(), InkSplashParticle.Factory::new);
        }

    }

}
