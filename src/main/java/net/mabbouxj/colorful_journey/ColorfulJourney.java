package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.client.entity.render.*;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.entities.*;
import net.mabbouxj.colorful_journey.events.BlockEvents;
import net.mabbouxj.colorful_journey.events.MobEvents;
import net.mabbouxj.colorful_journey.events.WorldEvents;
import net.mabbouxj.colorful_journey.init.*;
import net.mabbouxj.colorful_journey.items.RubiksCubeUnfinishedItem;
import net.mabbouxj.colorful_journey.utils.Multicolor;
import net.minecraft.block.Block;
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
import net.minecraft.util.ResourceLocation;
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

    public static final Map<Item, Map<DyeColor, RegistryObject<? extends Item>>> REPLACEMENT_ITEMS = new HashMap<>();
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
        ModEntityTypes.register(bus);
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
        REPLACEMENT_ITEMS.put(Items.HONEYCOMB, ModItems.COLORED_HONEYCOMBS);
        REPLACEMENT_ITEMS.put(Items.FEATHER, ModItems.COLORED_FEATHERS);
        REPLACEMENT_ITEMS.put(Items.LEATHER, ModItems.COLORED_LEATHERS);
        REPLACEMENT_ITEMS.put(Items.ENDER_PEARL, ModItems.COLORED_ENDER_PEARLS);
        REPLACEMENT_ITEMS.put(Items.BAMBOO, ModItems.COLORED_BAMBOOS);
        REPLACEMENT_ITEMS.put(Items.BONE, ModItems.COLORED_BONES);
        REPLACEMENT_ITEMS.put(Items.SKELETON_SKULL, ModItems.COLORED_SKULLS);
        REPLACEMENT_ITEMS.put(Items.STRING, ModItems.COLORED_STRINGS);
        REPLACEMENT_ITEMS.put(Items.WITHER_SKELETON_SKULL, ModItems.COLORED_SKULLS);
        REPLACEMENT_ITEMS.put(Items.ROTTEN_FLESH, ModItems.COLORED_ROTTEN_FLESHES);
        REPLACEMENT_ITEMS.put(Items.ZOMBIE_HEAD, ModItems.COLORED_SKULLS);
        REPLACEMENT_ITEMS.put(Items.NETHER_STAR, ModItems.COLORED_NETHER_STARS);
        REPLACEMENT_ITEMS.put(Items.GUNPOWDER, ModItems.COLORED_GUNPOWDERS);
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
        REPLACEMENT_MOBS.put(CreeperEntity.class, ColoredCreeperEntity.class);
    }

    @Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Common {
        private Common() {
        }

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(new MobEvents());
            MinecraftForge.EVENT_BUS.register(new BlockEvents());
            MinecraftForge.EVENT_BUS.register(new WorldEvents());
        }

        @SubscribeEvent
        public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
            for (DyeColor color: ColorfulJourney.COLORS) {
                event.put(ModEntityTypes.COLORED_CHICKEN.get(color).get(), ColoredChickenEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_BEE.get(color).get(), ColoredBeeEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_SKELETON.get(color).get(), ColoredSkeletonEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_COW.get(color).get(), ColoredCowEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_PANDA.get(color).get(), ColoredPandaEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_ZOMBIE.get(color).get(), ColoredZombieEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_SPIDER.get(color).get(), ColoredSpiderEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_ENDERMAN.get(color).get(), ColoredEndermanEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_WITHER_SKELETON.get(color).get(), ColoredWitherSkeletonEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_WITHER.get(color).get(), ColoredWitherEntity.createAttributes(color).build());
                event.put(ModEntityTypes.COLORED_CREEPER.get(color).get(), ColoredCreeperEntity.createAttributes(color).build());
            }
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Client {
        private Client() {
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.INK_BALL.get(), erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));

            for (DyeColor color: ColorfulJourney.COLORS) {
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_CHICKEN.get(color).get(), ColoredChickenRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_BEE.get(color).get(), ColoredBeeRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_SKELETON.get(color).get(), ColoredSkeletonRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_COW.get(color).get(), ColoredCowRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_PANDA.get(color).get(), ColoredPandaRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_ZOMBIE.get(color).get(), ColoredZombieRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_SPIDER.get(color).get(), ColoredSpiderRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_ENDERMAN.get(color).get(), ColoredEndermanRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_WITHER_SKELETON.get(color).get(), ColoredWitherSkeletonRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_WITHER.get(color).get(), ColoredWitherRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COLORED_CREEPER.get(color).get(), ColoredCreeperRenderer::new);

                RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_SKULLS.get(color).get(), RenderType.solid());
                RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_WALL_SKULLS.get(color).get(), RenderType.solid());
                RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_LOGS.get(color).get(), RenderType.solid());
                RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_LEAVES.get(color).get(), RenderType.cutout());
                RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_SAPLINGS.get(color).get(), RenderType.cutout());
            }

            RenderTypeLookup.setRenderLayer(ModBlocks.RUBIKS_CUBE.get(), RenderType.solid());
            RenderTypeLookup.setRenderLayer(ModBlocks.RUBIKS_CUBE_UNFINISHED.get(), RenderType.solid());

            event.enqueueWork(() -> {
                ItemModelsProperties.register(
                        ModItems.RUBIKS_CUBE_UNFINISHED.get(),
                        new ResourceLocation(MOD_ID, "mix"),
                        (stack, world, player) -> stack.getItem() instanceof RubiksCubeUnfinishedItem ? RubiksCubeUnfinishedItem.getMixVariant(stack): 0
                );
            });

        }

        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event) {
            for (RegistryObject<Item> registryItem : ModItems.ALL_COLORED_VARIANTS_ITEMS) {
                event.getItemColors().register(new Multicolor.Item(), registryItem.get());
            }
            for (RegistryObject<BlockItem> registryBlockItem : ModItems.ALL_COLORED_VARIANTS_BLOCK_ITEMS) {
                event.getItemColors().register(new Multicolor.Item(), registryBlockItem.get());
            }
            for (RegistryObject<Block> registryBlock: ModBlocks.ALL_COLORED_VARIANTS_BLOCKS) {
                event.getBlockColors().register(new Multicolor.Block(), registryBlock.get());
            }
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
