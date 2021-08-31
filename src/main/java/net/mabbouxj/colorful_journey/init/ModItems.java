package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulJourney.MOD_ID);

    public static final List<RegistryObject<Item>> COLORED_VARIANTS_ITEMS = new ArrayList<>();
    public static final List<RegistryObject<BlockItem>> COLORED_VARIANTS_BLOCK_ITEMS = new ArrayList<>();

    public static final RegistryObject<Item> COLOR_GUN = ITEMS.register("color_gun", ColorGunItem::new);
    public static final RegistryObject<Item> INK_BALL = ITEMS.register("ink_ball", () -> new InkBallItem(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64)));

    public static final RegistryObject<Item> COLORED_FEATHER = registerColoredVariantsItem("colored_feather");
    public static final RegistryObject<Item> COLORED_EGG = registerColoredVariantsItem("colored_egg");
    public static final RegistryObject<Item> COLORED_HONEYCOMB = registerColoredVariantsItem("colored_honeycomb");
    public static final RegistryObject<Item> COLORED_BONE = registerColoredVariantsItem("colored_bone");
    public static final RegistryObject<Item> COLORED_LEATHER = registerColoredVariantsItem("colored_leather");
    public static final RegistryObject<Item> COLORED_BAMBOO = registerColoredVariantsItem("colored_bamboo");
    public static final RegistryObject<Item> COLORED_ROTTEN_FLESH = registerColoredVariantsItem("colored_rotten_flesh");
    public static final RegistryObject<Item> COLORED_STRING = registerColoredVariantsItem("colored_string");
    public static final RegistryObject<Item> COLORED_ENDER_PEARL = registerColoredVariantsItem("colored_ender_pearl");
    public static final RegistryObject<Item> COLORED_NETHER_STAR = registerColoredVariantsItem("colored_nether_star");
    public static final RegistryObject<Item> COLORED_GUNPOWDER = registerColoredVariantsItem("colored_gunpowder");

    public static final RegistryObject<BlockItem> COLORED_SKULL = registerColoredVariantsWallOrFloorItem("colored_skull", ModBlocks.COLORED_SKULL, ModBlocks.COLORED_WALL_SKULL);


    private static RegistryObject<Item> registerColoredVariantsItem(String name) {
        Item.Properties props = new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64);
        RegistryObject<Item> itemRegistryObject = ITEMS.register(name, () -> new ColoredVariantsItem(props, name));
        COLORED_VARIANTS_ITEMS.add(itemRegistryObject);
        return itemRegistryObject;
    }

    private static RegistryObject<BlockItem> registerColoredVariantsBlockItem(String name, RegistryObject<Block> block) {
        Item.Properties props = new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64);
        RegistryObject<BlockItem> itemRegistryObject = ITEMS.register(name, () -> new ColoredVariantsBlockItem(block.get(), props, name));
        COLORED_VARIANTS_BLOCK_ITEMS.add(itemRegistryObject);
        return itemRegistryObject;
    }

    private static RegistryObject<BlockItem> registerColoredVariantsWallOrFloorItem(String name, RegistryObject<Block> floorBlock, RegistryObject<Block> wallBlock) {
        Item.Properties props = new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64);
        RegistryObject<BlockItem> itemRegistryObject = ITEMS.register(name, () -> new ColoredVariantsWallOrFloorItem(floorBlock.get(), wallBlock.get(), props, name));
        COLORED_VARIANTS_BLOCK_ITEMS.add(itemRegistryObject);
        return itemRegistryObject;
    }


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
