package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredLeavesBlock extends LeavesBlock {

    public ColoredLeavesBlock() {
        super(AbstractBlock.Properties
                .of(Material.LEAVES)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn(ColoredLeavesBlock::ocelotOrParrot)
                .isSuffocating(ColoredLeavesBlock::never)
                .isViewBlocking(ColoredLeavesBlock::never));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(DISTANCE, 7)
                .setValue(PERSISTENT, false)
                .setValue(STATE_COLOR_ID, DyeColor.WHITE.getId())
        );
    }

    private static Boolean ocelotOrParrot(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }

    private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(DISTANCE, PERSISTENT, STATE_COLOR_ID);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState blockState = super.getStateForPlacement(useContext);
        if (blockState == null) {
            blockState = defaultBlockState();
        }
        return blockState.setValue(STATE_COLOR_ID, ColorUtils.getColor(useContext.getItemInHand()).getId());
    }

}
