package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulJourney.MOD_ID);

    public static final List<RegistryObject<Item>> ALL_COLORED_VARIANTS_ITEMS = new ArrayList<>();
    public static final List<RegistryObject<BlockItem>> ALL_COLORED_VARIANTS_BLOCK_ITEMS = new ArrayList<>();

    public static final RegistryObject<Item> COLOR_GUN = ITEMS.register("color_gun", ColorGunItem::new);
    public static final RegistryObject<Item> COLOR_LASER_GUN = ITEMS.register("color_laser_gun", ColorLaserGunItem::new);
    public static final RegistryObject<Item> INK_BALL = ITEMS.register("ink_ball", InkBallItem::new);
    public static final RegistryObject<BlockItem> RUBIKS_CUBE = ITEMS.register("rubiks_cube", RubiksCubeItem::new);
    public static final RegistryObject<BlockItem> RUBIKS_CUBE_UNFINISHED = ITEMS.register("rubiks_cube_unfinished", RubiksCubeUnfinishedItem::new);

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_FEATHERS = registerColoredVariantsItem("colored_feather");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_EGGS = registerColoredVariantsItem("colored_egg");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_HONEYCOMBS = registerColoredVariantsItem("colored_honeycomb");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_BONES = registerColoredVariantsItem("colored_bone");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LEATHERS = registerColoredVariantsItem("colored_leather");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_BAMBOOS = registerColoredVariantsItem("colored_bamboo");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ROTTEN_FLESHES = registerColoredVariantsItem("colored_rotten_flesh");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRINGS = registerColoredVariantsItem("colored_string");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ENDER_PEARLS = registerColoredVariantsItem("colored_ender_pearl");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_NETHER_STARS = registerColoredVariantsItem("colored_nether_star");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_GUNPOWDERS = registerColoredVariantsItem("colored_gunpowder");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_INGOTS = registerColoredVariantsItem("colored_ingot");
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_NUGGETS = registerColoredVariantsItem("colored_nugget");

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_SKULLS = registerColoredVariantsWallOrFloorItem("colored_skull", ModBlocks.COLORED_SKULLS, ModBlocks.COLORED_WALL_SKULLS);

    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LOGS = registerColoredVariantsBlockItem("colored_log", ModBlocks.COLORED_LOGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRIPPED_LOGS = registerColoredVariantsBlockItem("colored_stripped_log", ModBlocks.COLORED_STRIPPED_LOGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_WOODS = registerColoredVariantsBlockItem("colored_wood", ModBlocks.COLORED_WOODS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_STRIPPED_WOODS = registerColoredVariantsBlockItem("colored_stripped_wood", ModBlocks.COLORED_STRIPPED_WOODS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_LEAVES = registerColoredVariantsBlockItem("colored_leaves", ModBlocks.COLORED_LEAVES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_SAPLINGS = registerColoredVariantsBlockItem("colored_sapling", ModBlocks.COLORED_SAPLINGS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_PLANKS = registerColoredVariantsBlockItem("colored_plank", ModBlocks.COLORED_PLANKS);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_ORES = registerColoredVariantsBlockItem("colored_ore", ModBlocks.COLORED_ORES);
    public static final Map<DyeColor, RegistryObject<? extends Item>> COLORED_INGOT_BLOCKS = registerColoredVariantsBlockItem("colored_ingot_block", ModBlocks.COLORED_INGOT_BLOCKS);

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsItem(String name) {
        Map<DyeColor, RegistryObject<? extends Item>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<Item> itemRegistryObject = ITEMS.register(
                    name + "_" + color.getName(),
                    () -> new ColoredVariantsItem(name, color));
            ALL_COLORED_VARIANTS_ITEMS.add(itemRegistryObject);
            map.put(color, itemRegistryObject);
        }
        return map;
    }

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsBlockItem(String name, Map<DyeColor, RegistryObject<Block>> blocks) {
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

    private static Map<DyeColor, RegistryObject<? extends Item>> registerColoredVariantsWallOrFloorItem(String name, Map<DyeColor, RegistryObject<Block>> floorBlocks, Map<DyeColor, RegistryObject<Block>> wallBlocks) {
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
