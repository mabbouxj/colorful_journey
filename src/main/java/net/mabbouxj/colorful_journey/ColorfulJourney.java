package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.entities.*;
import net.mabbouxj.colorful_journey.init.*;
import net.mabbouxj.colorful_journey.world.gen.OreGeneration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static net.mabbouxj.colorful_journey.ColorfulJourney.MOD_ID;


@Mod(MOD_ID)
public class ColorfulJourney {

    public static final String MOD_ID = "colorful_journey";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    public static final String MOD_NAME = "Colorful Journey";
    public static final String MOD_VERSION = "0.0.1";
    public static final ItemGroup MOD_ITEM_GROUP = new ColorfulJourneyItemGroup();

    public static final Map<Item, Map<DyeColor, RegistryObject<? extends Item>>> REPLACEMENT_ITEMS = new HashMap<>();
    public static final Map<Class<? extends Entity>, Class<? extends Entity>> REPLACEMENT_MOBS = new HashMap<>();
    public static final Set<DyeColor> ENABLED_COLORS = new HashSet<>();

    public ColorfulJourney() throws Exception {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Load configs
        final Pair<ModConfigs.Common, ForgeConfigSpec> specPairCommon = new ForgeConfigSpec.Builder().configure(ModConfigs.Common::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPairCommon.getRight());
        ModConfigs.COMMON_CONFIG = specPairCommon.getLeft();
        final Pair<ModConfigs.Client, ForgeConfigSpec> specPairClient = new ForgeConfigSpec.Builder().configure(ModConfigs.Client::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPairClient.getRight());
        ModConfigs.CLIENT_CONFIG = specPairClient.getLeft();

        initEnabledColors();

        // Register all stuff
        bus.register(this);
        ModBlocks.register(bus);
        ModTiles.register(bus);
        ModContainers.register(bus);
        ModItems.register(bus);
        ModEntityTypes.register(bus);
        ModSounds.register(bus);
        ModParticles.register(bus);
        ModRecipeSerializers.register(bus);

        bus.addListener(CommonEventBusSubscriber::onCommonSetup);
        bus.addListener(CommonEventBusSubscriber::onEntityAttributeCreationEvent);
        bus.addListener(CommonEventBusSubscriber::onClientSetup);
        bus.addListener(ClientEventBusSubscriber::onClientSetup);
        bus.addListener(ClientEventBusSubscriber::onColorHandlerEvent);
        bus.addListener(ClientEventBusSubscriber::onParticleFactoryRegistrationEvent);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGeneration::generateOres);

        populateReplacementItems();
        populateReplacementMobs();
    }

    private void initEnabledColors() throws Exception {
        for (String colorName: ModConfigs.COMMON_CONFIG.ENABLED_COLORS.get()) {
            DyeColor color = DyeColor.byName(colorName, DyeColor.WHITE);
            if (!colorName.equals(color.getName())) {
                throw new Exception("Error while loading enabled colors from configuration file, unknown color: " + colorName);
            }
            ENABLED_COLORS.add(color);
        }
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
