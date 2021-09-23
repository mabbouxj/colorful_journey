package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.entities.*;
import net.mabbouxj.colorful_journey.init.*;
import net.mabbouxj.colorful_journey.world.gen.OreGeneration;
import net.mabbouxj.colorful_journey.world.gen.TreeGeneration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
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
import net.minecraftforge.registries.ForgeRegistries;
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

    public static final Map<RegistryObject<? extends Item>, Map<DyeColor, RegistryObject<? extends Item>>> REPLACEMENT_ITEMS = new HashMap<>();
    public static final Map<Block, Map<DyeColor, RegistryObject<? extends Block>>> REPLACEMENT_BLOCKS = new HashMap<>();
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
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TreeGeneration::generateTrees);

        populateReplacementItems();
        populateReplacementBlocks();
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
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.HONEYCOMB.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_HONEYCOMBS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.FEATHER.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_FEATHERS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.LEATHER.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_LEATHERS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.ENDER_PEARL.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_ENDER_PEARLS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.BAMBOO.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_BAMBOOS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.BONE.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_BONES);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.STRING.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_STRINGS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.WITHER_SKELETON_SKULL.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_SKULLS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.ROTTEN_FLESH.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_ROTTEN_FLESHES);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.NETHER_STAR.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_NETHER_STARS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.GUNPOWDER.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_GUNPOWDERS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.PHANTOM_MEMBRANE.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_PHANTOM_MEMBRANE);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.GHAST_TEAR.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_GHAST_TEARS);
        REPLACEMENT_ITEMS.put(RegistryObject.of(Items.BLAZE_ROD.getRegistryName(), ForgeRegistries.ITEMS), ModItems.COLORED_BLAZE_RODS);
        REPLACEMENT_ITEMS.put(ModItems.BAT_WING, ModItems.COLORED_BAT_WINGS);
        REPLACEMENT_ITEMS.put(ModItems.TENTACLE, ModItems.COLORED_TENTACLES);
    }

    private void populateReplacementBlocks() {
        REPLACEMENT_BLOCKS.put(Blocks.DIRT, ModBlocks.COLORED_GRASS_BLOCKS);
        REPLACEMENT_BLOCKS.put(Blocks.GRASS_BLOCK, ModBlocks.COLORED_GRASS_BLOCKS);
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
        REPLACEMENT_MOBS.put(PhantomEntity.class, ColoredPhantomEntity.class);
        REPLACEMENT_MOBS.put(GhastEntity.class, ColoredGhastEntity.class);
        REPLACEMENT_MOBS.put(BlazeEntity.class, ColoredBlazeEntity.class);
        REPLACEMENT_MOBS.put(BatEntity.class, ColoredBatEntity.class);
        REPLACEMENT_MOBS.put(SquidEntity.class, ColoredSquidEntity.class);
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
