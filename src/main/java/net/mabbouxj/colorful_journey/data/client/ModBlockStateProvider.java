package net.mabbouxj.colorful_journey.data.client;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Generate more complex block states
        for (DyeColor color: ColorfulJourney.COLORS) {

            ModelFile coloredLogModelFile = new ModelFile.ExistingModelFile(modLoc("block/colored_log"), this.models().existingFileHelper);
            ModelFile coloredLogHorizontalModelFile = new ModelFile.ExistingModelFile(modLoc("block/colored_log_horizontal"), this.models().existingFileHelper);
            getVariantBuilder(ModBlocks.COLORED_LOGS.get(color).get())
                    .partialState()
                    .with(BlockStateProperties.AXIS, Direction.Axis.X)
                    .setModels(new ConfiguredModel(coloredLogHorizontalModelFile, 90, 90, false))
                    .partialState()
                    .with(BlockStateProperties.AXIS, Direction.Axis.Y)
                    .setModels(new ConfiguredModel(coloredLogModelFile, 0, 0, false))
                    .partialState()
                    .with(BlockStateProperties.AXIS, Direction.Axis.Z)
                    .setModels(new ConfiguredModel(coloredLogHorizontalModelFile, 90, 0, false));

            // Generate blockstates file with all possible values of rotation for skull
            ModelFile coloredSkullModelFile = new ModelFile.ExistingModelFile(modLoc("block/colored_skull"), this.models().existingFileHelper);
            int rotationY = 0;
            for (int i = 0; i < 16; i++) {
                if (i % 4 == 3) {
                    rotationY = (rotationY + 90) % 360;
                }
                ConfiguredModel modelForState = new ConfiguredModel(coloredSkullModelFile, 0, rotationY, false);
                getVariantBuilder(ModBlocks.COLORED_SKULLS.get(color).get())
                        .partialState()
                        .with(BlockStateProperties.ROTATION_16, i)
                        .setModels(modelForState);
            }

            // Generate blockstates file with all possible values of facing for wall skull
            ModelFile coloredWallSkullModelFile = new ModelFile.ExistingModelFile(modLoc("block/colored_wall_skull"), this.models().existingFileHelper);
            List<Direction> faces = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
            for (int i = 0; i < faces.size(); i++) {
                ConfiguredModel modelForState = new ConfiguredModel(coloredWallSkullModelFile, 0, i*90, false);
                getVariantBuilder(ModBlocks.COLORED_WALL_SKULLS.get(color).get())
                        .partialState()
                        .with(BlockStateProperties.HORIZONTAL_FACING, faces.get(i))
                        .setModels(modelForState);
            }

        }

        // Generate simple blocks (no state variants): AKA the rest
        for (RegistryObject<Block> registryObject: ModBlocks.ALL_COLORED_VARIANTS_BLOCKS) {
            try {
                String blockModelName = ColorUtils.removeColorSuffix(registryObject.get().getRegistryName().getPath());
                ModelFile blockModel = new ModelFile.ExistingModelFile(modLoc("block/" + blockModelName), this.models().existingFileHelper);
                simpleBlock(registryObject.get(), blockModel);
            } catch (Exception e) {
                Logger.getLogger(this.getName()).info("blockstates already generated for: " + registryObject.get().getRegistryName().toString());
            }
        }

    }
}
