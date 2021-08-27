package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.client.entity.render.*;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.entities.*;
import net.mabbouxj.colorful_journey.events.MobEvent;
import net.mabbouxj.colorful_journey.init.*;
import net.mabbouxj.colorful_journey.utils.Multicolor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.*;
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static net.mabbouxj.colorful_journey.ColorfulJourney.MOD_ID;


@Mod(MOD_ID)
public class ColorfulJourney {

    public static final String MOD_ID = "colorful_journey";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    public static final String MOD_NAME = "Colorful Journey";
    public static final String MOD_VERSION = "0.0.1";
    public static final ItemGroup MOD_ITEM_GROUP = new ColorfulJourneyItemGroup();
    public static final String NBT_COLOR_ID = "color";

    public static final Map<Item, RegistryObject<? extends Item>> REPLACEMENT_ITEMS = new HashMap<>();
    public static final Map<Class<? extends Entity>, Class<? extends Entity>> REPLACEMENT_MOBS = new HashMap<>();
    public static final DyeColor[] COLORS = new DyeColor[]{
            DyeColor.RED,
            DyeColor.GREEN,
            DyeColor.BLUE,
            DyeColor.CYAN,
            DyeColor.PINK,
            DyeColor.ORANGE,
            DyeColor.LIME
    };

    public ColorfulJourney() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.register(this);
        ModBlocks.register(bus);
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

        populateReplacementItems();
        populateReplacementMobs();
    }

    private void populateReplacementItems() {
        REPLACEMENT_ITEMS.put(Items.HONEYCOMB, ModItems.COLORED_HONEYCOMB);
        REPLACEMENT_ITEMS.put(Items.FEATHER, ModItems.COLORED_FEATHER);
        REPLACEMENT_ITEMS.put(Items.LEATHER, ModItems.COLORED_LEATHER);
        REPLACEMENT_ITEMS.put(Items.ENDER_PEARL, ModItems.COLORED_ENDER_PEARL);
        REPLACEMENT_ITEMS.put(Items.BAMBOO, ModItems.COLORED_BAMBOO);
        REPLACEMENT_ITEMS.put(Items.BONE, ModItems.COLORED_BONE);
        REPLACEMENT_ITEMS.put(Items.SKELETON_SKULL, ModItems.COLORED_SKULL);
        REPLACEMENT_ITEMS.put(Items.STRING, ModItems.COLORED_STRING);
        REPLACEMENT_ITEMS.put(Items.WITHER_SKELETON_SKULL, ModItems.COLORED_SKULL);
        REPLACEMENT_ITEMS.put(Items.ROTTEN_FLESH, ModItems.COLORED_ROTTEN_FLESH);
        REPLACEMENT_ITEMS.put(Items.ZOMBIE_HEAD, ModItems.COLORED_SKULL);
        REPLACEMENT_ITEMS.put(Items.NETHER_STAR, ModItems.COLORED_NETHER_STAR);
    }

    private void populateReplacementMobs() {
        REPLACEMENT_MOBS.put(BeeEntity.class, ColoredBeeEntity.class);
        REPLACEMENT_MOBS.put(ChickenEntity.class, ColoredChickenEntity.class);
        REPLACEMENT_MOBS.put(CowEntity.class, ColoredCowEntity.class);
        REPLACEMENT_MOBS.put(EndermanEntity.class, ColoredEndermanEntity.class);
        REPLACEMENT_MOBS.put(PandaEntity.class, ColoredPandaEntity.class);
        REPLACEMENT_MOBS.put(SkeletonEntity.class, ColoredSkeletonEntity.class);
        REPLACEMENT_MOBS.put(SpiderEntity.class, ColoredSpiderEntity.class);
        REPLACEMENT_MOBS.put(WitherSkeletonEntity.class, ColoredWitherSkeletonEntity.class);
        REPLACEMENT_MOBS.put(ZombieEntity.class, ColoredZombieEntity.class);
        REPLACEMENT_MOBS.put(WitherEntity.class, ColoredWitherEntity.class);
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
            event.put(ModEntities.COLORED_SPIDER.get(), SpiderEntity.createAttributes().build());
            event.put(ModEntities.COLORED_ENDERMAN.get(), EndermanEntity.createAttributes().build());
            event.put(ModEntities.COLORED_WITHER_SKELETON.get(), WitherSkeletonEntity.createAttributes().build());
            event.put(ModEntities.COLORED_WITHER.get(), WitherEntity.createAttributes().build());
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
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_SPIDER.get(), ColoredSpiderRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_ENDERMAN.get(), ColoredEndermanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_WITHER_SKELETON.get(), ColoredWitherSkeletonRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.COLORED_WITHER.get(), ColoredWitherRenderer::new);

            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_SKULL.get(), RenderType.solid());
        }

        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event) {
            for (RegistryObject<Item> registryItem : ModItems.COLORED_VARIANTS_ITEMS) {
                event.getItemColors().register(new Multicolor.Item(), registryItem.get());
            }
            for (RegistryObject<BlockItem> registryBlockItem : ModItems.COLORED_VARIANTS_BLOCK_ITEMS) {
                event.getItemColors().register(new Multicolor.Item(), registryBlockItem.get());
            }
            event.getBlockColors().register(new Multicolor.Block(), ModBlocks.COLORED_SKULL.get());
        }

        @SubscribeEvent
        public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particleEngine.register(ModParticles.INK_SPLASH.get(), InkSplashParticle.Factory::new);
        }

    }

    private static class ColorfulJourneyItemGroup extends ItemGroup {
        public ColorfulJourneyItemGroup() {
            super(ColorfulJourney.MOD_ID);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.INK_BALL.get());
        }
    }

}
