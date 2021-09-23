package net.mabbouxj.colorful_journey.data.tag;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Map;

public class ModItemTagProvider extends ItemTagsProvider {

    public static final ITag.INamedTag<Item> COLORED_INGOTS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_ingots");
    public static final ITag.INamedTag<Item> COLORED_NUGGETS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_nuggets");
    public static final ITag.INamedTag<Item> COLORED_FEATHERS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_feathers");
    public static final ITag.INamedTag<Item> COLORED_EGGS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_eggs");
    public static final ITag.INamedTag<Item> COLORED_HONEYCOMBS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_honeycombs");
    public static final ITag.INamedTag<Item> COLORED_BONES = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_bones");
    public static final ITag.INamedTag<Item> COLORED_LEATHERS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_leathers");
    public static final ITag.INamedTag<Item> COLORED_BAMBOOS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_bamboos");
    public static final ITag.INamedTag<Item> COLORED_ROTTEN_FLESHES = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_rotten_fleshes");
    public static final ITag.INamedTag<Item> COLORED_STRINGS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_strings");
    public static final ITag.INamedTag<Item> COLORED_ENDER_PEARLS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_ender_pearls");
    public static final ITag.INamedTag<Item> COLORED_NETHER_STARS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_nether_stars");
    public static final ITag.INamedTag<Item> COLORED_GUNPOWDERS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_gunpowders");
    public static final ITag.INamedTag<Item> COLORED_PHANTOM_MEMBRANES = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_phantom_membranes");
    public static final ITag.INamedTag<Item> COLORED_GHAST_TEARS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_ghast_tears");
    public static final ITag.INamedTag<Item> COLORED_BLAZE_RODS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_blaze_rods");
    public static final ITag.INamedTag<Item> COLORED_SKULLS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_skulls");
    public static final ITag.INamedTag<Item> COLORED_BAT_WINGS = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_bat_wings");
    public static final ITag.INamedTag<Item> COLORED_TENTACLES = ItemTags.bind(ColorfulJourney.MOD_ID + ":colored_tentacles");

    public ModItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Items.FEATHERS).add(mapToItem(ModItems.COLORED_FEATHERS));
        tag(Tags.Items.EGGS).add(mapToItem(ModItems.COLORED_EGGS));
        tag(Tags.Items.BONES).add(mapToItem(ModItems.COLORED_BONES));
        tag(Tags.Items.LEATHER).add(mapToItem(ModItems.COLORED_LEATHERS));
        tag(Tags.Items.STRING).add(mapToItem(ModItems.COLORED_STRINGS));
        tag(Tags.Items.ENDER_PEARLS).add(mapToItem(ModItems.COLORED_ENDER_PEARLS));
        tag(Tags.Items.NETHER_STARS).add(mapToItem(ModItems.COLORED_NETHER_STARS));
        tag(Tags.Items.GUNPOWDER).add(mapToItem(ModItems.COLORED_GUNPOWDERS));
        tag(Tags.Items.HEADS).add(mapToItem(ModItems.COLORED_SKULLS));
        tag(COLORED_INGOTS).add(mapToItem(ModItems.COLORED_INGOTS));
        tag(COLORED_NUGGETS).add(mapToItem(ModItems.COLORED_NUGGETS));
        tag(COLORED_FEATHERS).add(mapToItem(ModItems.COLORED_FEATHERS));
        tag(COLORED_EGGS).add(mapToItem(ModItems.COLORED_EGGS));
        tag(COLORED_HONEYCOMBS).add(mapToItem(ModItems.COLORED_HONEYCOMBS));
        tag(COLORED_BONES).add(mapToItem(ModItems.COLORED_BONES));
        tag(COLORED_LEATHERS).add(mapToItem(ModItems.COLORED_LEATHERS));
        tag(COLORED_BAMBOOS).add(mapToItem(ModItems.COLORED_BAMBOOS));
        tag(COLORED_ROTTEN_FLESHES).add(mapToItem(ModItems.COLORED_ROTTEN_FLESHES));
        tag(COLORED_STRINGS).add(mapToItem(ModItems.COLORED_STRINGS));
        tag(COLORED_ENDER_PEARLS).add(mapToItem(ModItems.COLORED_ENDER_PEARLS));
        tag(COLORED_NETHER_STARS).add(mapToItem(ModItems.COLORED_NETHER_STARS));
        tag(COLORED_GUNPOWDERS).add(mapToItem(ModItems.COLORED_GUNPOWDERS));
        tag(COLORED_PHANTOM_MEMBRANES).add(mapToItem(ModItems.COLORED_PHANTOM_MEMBRANE));
        tag(COLORED_GHAST_TEARS).add(mapToItem(ModItems.COLORED_GHAST_TEARS));
        tag(COLORED_BLAZE_RODS).add(mapToItem(ModItems.COLORED_BLAZE_RODS));
        tag(COLORED_SKULLS).add(mapToItem(ModItems.COLORED_SKULLS));
        tag(COLORED_BAT_WINGS).add(mapToItem(ModItems.COLORED_BAT_WINGS));
        tag(COLORED_TENTACLES).add(mapToItem(ModItems.COLORED_TENTACLES));
    }

    private Item[] mapToItem(Map<DyeColor, RegistryObject<? extends Item>> map) {
        return map.values().stream().map(RegistryObject::get).toArray(Item[]::new);
    }

}
