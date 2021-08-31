package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.world.ColoredTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.StateContainer;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredSaplingBlock extends SaplingBlock {

    public ColoredSaplingBlock() {
        super(new ColoredTree(), AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STATE_COLOR_ID, DyeColor.WHITE.getId())
                .setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(STAGE, STATE_COLOR_ID);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return defaultBlockState()
                .setValue(STATE_COLOR_ID, ColorUtils.getColor(useContext.getItemInHand()).getId());
    }

}
