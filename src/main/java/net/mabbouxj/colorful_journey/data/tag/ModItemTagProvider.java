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

    public ModItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Items.FEATHERS)
                .add(mapToItem(ModItems.COLORED_FEATHERS));
        tag(Tags.Items.EGGS)
                .add(mapToItem(ModItems.COLORED_EGGS));
        tag(Tags.Items.BONES)
                .add(mapToItem(ModItems.COLORED_BONES));
        tag(Tags.Items.LEATHER)
                .add(mapToItem(ModItems.COLORED_LEATHERS));
        tag(Tags.Items.STRING)
                .add(mapToItem(ModItems.COLORED_STRINGS));
        tag(Tags.Items.ENDER_PEARLS)
                .add(mapToItem(ModItems.COLORED_ENDER_PEARLS));
        tag(Tags.Items.NETHER_STARS)
                .add(mapToItem(ModItems.COLORED_NETHER_STARS));
        tag(Tags.Items.GUNPOWDER)
                .add(mapToItem(ModItems.COLORED_GUNPOWDERS));
        tag(Tags.Items.HEADS)
                .add(mapToItem(ModItems.COLORED_SKULLS));
        tag(COLORED_INGOTS)
                .add(mapToItem(ModItems.COLORED_INGOTS));
    }

    private Item[] mapToItem(Map<DyeColor, RegistryObject<? extends Item>> map) {
        return map.values().stream().map(RegistryObject::get).toArray(Item[]::new);
    }

}
