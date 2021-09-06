package net.mabbouxj.colorful_journey.data.client;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.List;


public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Generate more complex block states
        for (DyeColor color: DyeColor.values()) {

            columnHorizontalBlock(ModBlocks.COLORED_LOGS.get(color).get(), modLoc("block/colored_log"), modLoc("block/colored_log_horizontal"));
            columnHorizontalBlock(ModBlocks.COLORED_STRIPPED_LOGS.get(color).get(), modLoc("block/colored_stripped_log"), modLoc("block/colored_stripped_log_horizontal"));
            columnHorizontalBlock(ModBlocks.COLORED_WOODS.get(color).get(), modLoc("block/colored_wood"), modLoc("block/colored_wood"));
            columnHorizontalBlock(ModBlocks.COLORED_STRIPPED_WOODS.get(color).get(), modLoc("block/colored_stripped_wood"), modLoc("block/colored_stripped_wood"));

            skullBlock(ModBlocks.COLORED_SKULLS.get(color).get(), modLoc("block/colored_skull"));
            wallSkullBlock(ModBlocks.COLORED_WALL_SKULLS.get(color).get(), modLoc("block/colored_wall_skull"));

        }

        // Generate simple blocks (no state variants): AKA the rest
        for (RegistryObject<Block> registryObject: ModBlocks.ALL_COLORED_VARIANTS_BLOCKS) {
            try {
                String blockModelName = ColorUtils.removeColorSuffix(registryObject.get().getRegistryName().getPath());
                ModelFile blockModel = new ModelFile.ExistingModelFile(modLoc("block/" + blockModelName), this.models().existingFileHelper);
                simpleBlock(registryObject.get(), blockModel);
            } catch (Exception ignored) { }
        }

    }

    private void wallSkullBlock(Block block, ResourceLocation model) {
        ModelFile coloredWallSkullModelFile = new ModelFile.ExistingModelFile(model, this.models().existingFileHelper);
        List<Direction> faces = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        for (int i = 0; i < faces.size(); i++) {
            ConfiguredModel modelForState = new ConfiguredModel(coloredWallSkullModelFile, 0, i*90, false);
            getVariantBuilder(block)
                    .partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, faces.get(i))
                    .setModels(modelForState);
        }
    }

    private void skullBlock(Block block, ResourceLocation model) {
        ModelFile skullModelFile = new ModelFile.ExistingModelFile(model, this.models().existingFileHelper);
        int rotationY = 0;
        for (int i = 0; i < 16; i++) {
            if (i % 4 == 3) {
                rotationY = (rotationY + 90) % 360;
            }
            ConfiguredModel modelForState = new ConfiguredModel(skullModelFile, 0, rotationY, false);
            getVariantBuilder(block)
                    .partialState()
                    .with(BlockStateProperties.ROTATION_16, i)
                    .setModels(modelForState);
        }
    }

    private void columnHorizontalBlock(Block block, ResourceLocation baseModel, ResourceLocation horizontalModel) {
        ModelFile baseModelFile = new ModelFile.ExistingModelFile(baseModel, this.models().existingFileHelper);
        ModelFile horizontalModelFile = new ModelFile.ExistingModelFile(horizontalModel, this.models().existingFileHelper);
        getVariantBuilder(block)
                .partialState()
                .with(BlockStateProperties.AXIS, Direction.Axis.X)
                .setModels(new ConfiguredModel(horizontalModelFile, 90, 90, false))
                .partialState()
                .with(BlockStateProperties.AXIS, Direction.Axis.Y)
                .setModels(new ConfiguredModel(baseModelFile, 0, 0, false))
                .partialState()
                .with(BlockStateProperties.AXIS, Direction.Axis.Z)
                .setModels(new ConfiguredModel(horizontalModelFile, 90, 0, false));
    }
}
