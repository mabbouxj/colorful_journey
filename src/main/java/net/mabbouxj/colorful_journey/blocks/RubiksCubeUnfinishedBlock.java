package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.items.RubiksCubeUnfinishedItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

public class RubiksCubeUnfinishedBlock extends RubiksCubeBlock {

    private static final IntegerProperty STATE_MIX_VARIANT = IntegerProperty.create("mix", 0, 3);

    public RubiksCubeUnfinishedBlock() {
        super();
        this.registerDefaultState(this.getStateDefinition().any().setValue(STATE_MIX_VARIANT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(STATE_MIX_VARIANT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        // Get the mix variant from the item stack NBT and use it as block variant
        // in order to keep the same mix between the held item and the block placed
        Integer mixVariant = RubiksCubeUnfinishedItem.getMixVariant(useContext.getItemInHand());
        return super.getStateForPlacement(useContext).setValue(STATE_MIX_VARIANT, mixVariant);
    }

}
