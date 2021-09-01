package net.mabbouxj.colorful_journey.data.tag;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Map;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(DataGenerator p_i48256_1_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_i48256_1_, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.LOGS)
                .add(mapToBlock(ModBlocks.COLORED_LOGS));
        tag(BlockTags.SAPLINGS)
                .add(mapToBlock(ModBlocks.COLORED_SAPLINGS));
        tag(BlockTags.LEAVES)
                .add(mapToBlock(ModBlocks.COLORED_LEAVES));
    }

    private Block[] mapToBlock(Map<DyeColor, RegistryObject<Block>> map) {
        return map.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

}
