package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.client.entity.render.*;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.items.RubiksCubeUnfinishedItem;
import net.mabbouxj.colorful_journey.utils.Multicolor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.INK_BALL.get(), erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));

        for (DyeColor color: DyeColor.values()) {
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
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_ORES.get(color).get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_NETHER_ORES.get(color).get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_END_ORES.get(color).get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_GRASS_BLOCKS.get(color).get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_GRASS_PATH.get(color).get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.COLORED_GRASS.get(color).get(), RenderType.cutout());
        }

        RenderTypeLookup.setRenderLayer(ModBlocks.RUBIKS_CUBE.get(), RenderType.solid());
        RenderTypeLookup.setRenderLayer(ModBlocks.RUBIKS_CUBE_UNFINISHED.get(), RenderType.solid());

        event.enqueueWork(() -> ItemModelsProperties.register(
                ModItems.RUBIKS_CUBE_UNFINISHED.get(),
                new ResourceLocation(ColorfulJourney.MOD_ID, "mix"),
                (stack, world, player) -> stack.getItem() instanceof RubiksCubeUnfinishedItem ? RubiksCubeUnfinishedItem.getMixVariant(stack): 0
        ));

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
    public static void onParticleFactoryRegistrationEvent(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.INK_SPLASH.get(), InkSplashParticle.Factory::new);
    }

}
