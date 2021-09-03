package net.mabbouxj.colorful_journey.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RubiksCubeBlock extends Block {

    private static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE = Block.box(1.16D, 1.14D, 1.16D, 14.84D, 14.82D, 14.84D);

    public RubiksCubeBlock() {
        super(AbstractBlock.Properties.of(Material.DECORATION).strength(0.5F).sound(SoundType.ANCIENT_DEBRIS));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return this.defaultBlockState().setValue(FACING, useContext.getNearestLookingDirection());
   }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

}
