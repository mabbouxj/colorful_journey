package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.items.ColorGunItem;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.mabbouxj.colorful_journey.items.InkBallItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<Item> COLOR_GUN = ITEMS.register("color_gun", ColorGunItem::new);
    public static final RegistryObject<Item> INK_BALL = ITEMS.register("ink_ball", () -> new InkBallItem(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64)));

    public static final List<RegistryObject<Item>> COLORFUL_ITEMS = new ArrayList<>();
    public static final RegistryObject<Item> COLORED_FEATHER = registerColorfulItem("colored_feather");
    public static final RegistryObject<Item> COLORED_EGG = registerColorfulItem("colored_egg");
    public static final RegistryObject<Item> COLORED_HONEYCOMB = registerColorfulItem("colored_honeycomb");
    public static final RegistryObject<Item> COLORED_BONE = registerColorfulItem("colored_bone");
    public static final RegistryObject<Item> COLORED_LEATHER = registerColorfulItem("colored_leather");
    public static final RegistryObject<Item> COLORED_BAMBOO = registerColorfulItem("colored_bamboo");
    public static final RegistryObject<Item> COLORED_ROTTEN_FLESH = registerColorfulItem("colored_rotten_flesh");
    public static final RegistryObject<Item> COLORED_STRING = registerColorfulItem("colored_string");
    public static final RegistryObject<Item> COLORED_ENDER_PEARL = registerColorfulItem("colored_ender_pearl");

    private static RegistryObject<Item> registerColorfulItem(String name) {
        Item.Properties props = new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64);
        RegistryObject<Item> itemRegistryObject = ITEMS.register(name, () -> new ColorfulItem(props, name));
        COLORFUL_ITEMS.add(itemRegistryObject);
        return itemRegistryObject;
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
