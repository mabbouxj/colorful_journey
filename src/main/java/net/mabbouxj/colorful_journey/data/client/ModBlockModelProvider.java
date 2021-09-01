package net.mabbouxj.colorful_journey.data.client;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModBlockModelProvider extends BlockModelProvider {

    public ModBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        for (RegistryObject<Block> registryObject: ModBlocks.ALL_COLORED_VARIANTS_BLOCKS) {
            ResourceLocation registry = registryObject.get().getRegistryName();
            String blockModelName = ColorUtils.removeColorSuffix(registry.getPath());
            ModelFile blockModel = getExistingFile(modLoc("block/" + blockModelName));
            getBuilder(registry.getPath()).parent(blockModel);
        }

    }



}
