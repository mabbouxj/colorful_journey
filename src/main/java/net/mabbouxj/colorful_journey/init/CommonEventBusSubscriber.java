package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.client.gui.EnergyCapacitorScreen;
import net.mabbouxj.colorful_journey.client.gui.EnergyDyeGeneratorScreen;
import net.mabbouxj.colorful_journey.entities.*;
import net.mabbouxj.colorful_journey.events.BlockEvents;
import net.mabbouxj.colorful_journey.events.MobEvents;
import net.mabbouxj.colorful_journey.events.WorldEvents;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventBusSubscriber {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ModConfigs.build();
        MinecraftForge.EVENT_BUS.register(new MobEvents());
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        DeferredWorkQueue.runLater(() -> {
            for (DyeColor color: DyeColor.values()) {
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
        });
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ScreenManager.register(ModContainers.ENERGY_DYE_GENERATOR.get(), EnergyDyeGeneratorScreen::new);
        ScreenManager.register(ModContainers.ENERGY_CAPACITOR.get(), EnergyCapacitorScreen::new);
    }

}
