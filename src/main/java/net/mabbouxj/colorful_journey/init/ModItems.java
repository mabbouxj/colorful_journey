package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.items.ColorGunItem;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.mabbouxj.colorful_journey.items.InkBallItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final RegistryObject<Item> COLOR_GUN = Registration.ITEMS.register("color_gun", ColorGunItem::new);
    public static final RegistryObject<Item> INK_BALL = Registration.ITEMS.register("ink_ball", () -> new InkBallItem(new Item.Properties().tab(Reference.MOD_ITEM_GROUP).stacksTo(64)));

    public static final List<RegistryObject<Item>> COLORFUL_ITEMS = new ArrayList<>();
    public static final RegistryObject<Item> COLORED_FEATHER = registerColorfulItem("colored_feather");
    public static final RegistryObject<Item> COLORED_EGG = registerColorfulItem("colored_egg");
    public static final RegistryObject<Item> COLORED_HONEYCOMB = registerColorfulItem("colored_honeycomb");
    public static final RegistryObject<Item> COLORED_BONE = registerColorfulItem("colored_bone");

    private static RegistryObject<Item> registerColorfulItem(String name) {
        Item.Properties props = new Item.Properties().tab(Reference.MOD_ITEM_GROUP).stacksTo(64);
        RegistryObject<Item> itemRegistryObject = Registration.ITEMS.register(name, () -> new ColorfulItem(props, name));
        COLORFUL_ITEMS.add(itemRegistryObject);
        return itemRegistryObject;
    }

    static void register() {
    }

}
