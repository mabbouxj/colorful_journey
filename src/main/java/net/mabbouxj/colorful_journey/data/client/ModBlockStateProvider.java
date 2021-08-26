package net.mabbouxj.colorful_journey.data.client;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;


public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Generate blockstates file with all possible values of rotation
        ModelFile coloredSkullModelFile = new ModelFile.ExistingModelFile(modLoc("block/colored_skull"), this.models().existingFileHelper);

        for (int i = 0; i < 16; i++) {
            int rotationY = 0;
            if (i >= 12) {
                rotationY = 270;
            } else if (i >= 8) {
                rotationY = 180;
            } else if (i >= 4) {
                rotationY = 90;
            }
            ConfiguredModel modelForState = new ConfiguredModel(coloredSkullModelFile, 0, rotationY, false);
            getVariantBuilder(ModBlocks.COLORED_SKULL.get())
                    .partialState()
                    .with(BlockStateProperties.ROTATION_16, i)
                    .setModels(modelForState);
        }


    }
}
