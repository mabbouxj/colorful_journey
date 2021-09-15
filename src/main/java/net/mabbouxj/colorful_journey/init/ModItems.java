package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulJourney.MOD_ID);

    public static final List<RegistryObject<Item>> ALL_COLORED_VARIANTS_ITEMS = new ArrayList<>();
    public static final List<RegistryObject<BlockItem>> ALL_COLORED_VARIANTS_BLOCK_ITEMS = new ArrayList<>();

    public static final RegistryObject<Item> HORSEHAIR = ITEMS.register("horsehair", () -> new Item(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP)));
    public static final RegistryObject<Item> PAINTBRUSH = ITEMS.register("paintbrush", PaintbrushItem::new);
    public static final RegistryObject<Item> COLOR_PALETTE = ITEMS.register("color_palette", ColorPaletteItem::new);
    public static final RegistryObject<Item> COLOR_GUN = ITEMS.register("color_gun", ColorGunItem::new);
    public static final RegistryObject<Item> COLOR_LASER_GUN = ITEMS.register("color_laser_gun", ColorLaserGunItem::new);
    public static final RegistryObject<Item> INK_BALL = ITEMS.register("ink_ball", InkBallItem::new);
    public static final RegistryObject<BlockItem> RUBIKS_CUBE = ITEMS.register("rubiks_cube", RubiksCubeItem::new);
    public static final RegistryObject<BlockItem> RUBIKS_CUBE_UNFINISHED = ITEMS.register("rubiks_cube_unfinished", RubiksCubeUnfinishedItem::new);
    public static final RegistryObject<BlockItem> ENERGY_DYE_GENERATOR = ITEMS.register("energy_dye_generator", EnergyDyeGeneratorBlockItem::new);
    public static final RegistryObject<BlockItem> ENERGY_CAPACITOR = ITEMS.register("energy_capacitor", EnergyCapacitorBlockItem::new);

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_FEATHERS = registerColoredVariantsItem("colored_feather", Items.FEATHER);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_EGGS = registerColoredVariantsItem("colored_egg", Items.EGG);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_HONEYCOMBS = registerColoredVariantsItem("colored_honeycomb", Items.HONEYCOMB);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_BONES = registerColoredVariantsItem("colored_bone", Items.BONE);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LEATHERS = registerColoredVariantsItem("colored_leather", Items.LEATHER);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_BAMBOOS = registerColoredVariantsItem("colored_bamboo", Items.BAMBOO);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ROTTEN_FLESHES = registerColoredVariantsItem("colored_rotten_flesh", Items.ROTTEN_FLESH);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRINGS = registerColoredVariantsItem("colored_string", Items.STRING);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ENDER_PEARLS = registerColoredVariantsItem("colored_ender_pearl", ColoredEnderPearlItem::new);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_NETHER_STARS = registerColoredVariantsItem("colored_nether_star", Items.NETHER_STAR);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GUNPOWDERS = registerColoredVariantsItem("colored_gunpowder", Items.GUNPOWDER);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_PHANTOM_MEMBRANE = registerColoredVariantsItem("colored_phantom_membrane", Items.PHANTOM_MEMBRANE);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GHAST_TEARS = registerColoredVariantsItem("colored_ghast_tear", Items.GHAST_TEAR);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_BLAZE_RODS = registerColoredVariantsItem("colored_blaze_rod", Items.BLAZE_ROD);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_INGOTS = registerColoredVariantsItem("colored_ingot", (Item) null);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_NUGGETS = registerColoredVariantsItem("colored_nugget", (Item) null);

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_SKULLS = registerColoredVariantsWallOrFloorItem("colored_skull", ModBlocks.COLORED_SKULLS, ModBlocks.COLORED_WALL_SKULLS);

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LOGS = registerColoredVariantsBlockItem("colored_log", ModBlocks.COLORED_LOGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRIPPED_LOGS = registerColoredVariantsBlockItem("colored_stripped_log", ModBlocks.COLORED_STRIPPED_LOGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_WOODS = registerColoredVariantsBlockItem("colored_wood", ModBlocks.COLORED_WOODS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRIPPED_WOODS = registerColoredVariantsBlockItem("colored_stripped_wood", ModBlocks.COLORED_STRIPPED_WOODS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LEAVES = registerColoredVariantsBlockItem("colored_leaves", ModBlocks.COLORED_LEAVES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_SAPLINGS = registerColoredVariantsBlockItem("colored_sapling", ModBlocks.COLORED_SAPLINGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_PLANKS = registerColoredVariantsBlockItem("colored_plank", ModBlocks.COLORED_PLANKS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ORES = registerColoredVariantsBlockItem("colored_ore", ModBlocks.COLORED_ORES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_NETHER_ORES = registerColoredVariantsBlockItem("colored_nether_ore", ModBlocks.COLORED_NETHER_ORES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_END_ORES = registerColoredVariantsBlockItem("colored_end_ore", ModBlocks.COLORED_END_ORES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_INGOT_BLOCKS = registerColoredVariantsBlockItem("colored_ingot_block", ModBlocks.COLORED_INGOT_BLOCKS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GRASS_BLOCKS = registerColoredVariantsBlockItem("colored_grass_block", ModBlocks.COLORED_GRASS_BLOCKS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GRASS_PATH = registerColoredVariantsBlockItem("colored_grass_path", ModBlocks.COLORED_GRASS_PATH);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GRASS = registerColoredVariantsBlockItem("colored_grass", ModBlocks.COLORED_GRASS);

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsItem(String name, Item initialItem) {
        Map<DyeColor, RegistryObject<? extends Item>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<Item> itemRegistryObject = ITEMS.register(name + "_" + color.getName(), () -> new ColoredVariantsItem(name, color, initialItem));
            ALL_COLORED_VARIANTS_ITEMS.add(itemRegistryObject);
            map.put(color, itemRegistryObject);
        }
        return map;
    }

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsItem(String name, Function<DyeColor, ? extends Item> supplier) {
        Map<DyeColor, RegistryObject<? extends Item>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<Item> itemRegistryObject = ITEMS.register(name + "_" + color.getName(), () -> supplier.apply(color));
            ALL_COLORED_VARIANTS_ITEMS.add(itemRegistryObject);
            map.put(color, itemRegistryObject);
        }
        return map;
    }

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsBlockItem(String name, Map<DyeColor, RegistryObject<? extends Block>> blocks) {
        Map<DyeColor, RegistryObject<? extends Item>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<BlockItem> itemRegistryObject = ITEMS.register(
                    name + "_" + color.getName(),
                    () -> new ColoredVariantsBlockItem(blocks.get(color).get(), name, color));
            ALL_COLORED_VARIANTS_BLOCK_ITEMS.add(itemRegistryObject);
            map.put(color, itemRegistryObject);
        }
        return map;
    }

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsWallOrFloorItem(String name, Map<DyeColor, RegistryObject<? extends Block>> floorBlocks, Map<DyeColor, RegistryObject<? extends Block>> wallBlocks) {
        Map<DyeColor, RegistryObject<? extends Item>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<BlockItem> itemRegistryObject = ITEMS.register(
                    name + "_" + color.getName(),
                    () -> new ColoredVariantsWallOrFloorItem(floorBlocks.get(color).get(), wallBlocks.get(color).get(), name, color));
            ALL_COLORED_VARIANTS_BLOCK_ITEMS.add(itemRegistryObject);
            map.put(color, itemRegistryObject);
        }
        return map;
    }


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
