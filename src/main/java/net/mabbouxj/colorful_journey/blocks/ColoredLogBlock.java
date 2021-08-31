package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import java.util.ArrayList;
import java.util.List;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredLogBlock extends RotatedPillarBlock {

    public ColoredLogBlock() {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(STATE_COLOR_ID, DyeColor.WHITE.getId())
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS);
        stateBuilder.add(STATE_COLOR_ID);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return defaultBlockState()
                .setValue(AXIS, useContext.getClickedFace().getAxis())
                .setValue(STATE_COLOR_ID, ColorUtils.getColor(useContext.getItemInHand()).getId());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        ItemStack stack = new ItemStack(ModItems.COLORED_LOG.get(), 1);
        ColorUtils.setColor(stack, DyeColor.byId(ColorUtils.getColorId(state)));
        drops.add(stack);
        return drops;
    }

}
