package net.mabbouxj.colorful_journey.data.tag;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Map;

public class ModBlockTagProvider extends BlockTagsProvider {

    public static final ITag.INamedTag<Block> COLORFUL_PORTAL_FRAME = BlockTags.bind(ColorfulJourney.MOD_ID + ":colorful_portal_frame");

    public ModBlockTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.LOGS).add(mapToBlock(ModBlocks.COLORED_LOGS));
        tag(BlockTags.SAPLINGS).add(mapToBlock(ModBlocks.COLORED_SAPLINGS));
        tag(BlockTags.LEAVES).add(mapToBlock(ModBlocks.COLORED_LEAVES));
        tag(Tags.Blocks.DIRT).add(mapToBlock(ModBlocks.COLORED_GRASS_BLOCKS));
        tag(COLORFUL_PORTAL_FRAME).add(mapToBlock(ModBlocks.COLORFUL_COLORED_PORTAL_FRAME));
        tag(COLORFUL_PORTAL_FRAME).add(ModBlocks.COLORFUL_PORTAL_FRAME.get());
    }

    private Block[] mapToBlock(Map<DyeColor, RegistryObject<? extends Block>> map) {
        return map.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

}
