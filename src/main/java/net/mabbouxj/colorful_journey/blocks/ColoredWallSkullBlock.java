package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredWallSkullBlock extends WallSkullBlock {

    public ColoredWallSkullBlock(SkullBlock.ISkullType skullType) {
        super(skullType, AbstractBlock.Properties.of(Material.DECORATION).strength(1.0F));
    }

    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack stack) {
        ModBlocks.COLORED_SKULL.get().setPlacedBy(world, blockPos, blockState, entity, stack);
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
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        BlockState blockState = super.getStateForPlacement(useContext);
        if (blockState == null) {
            blockState = this.defaultBlockState();
        }
        return blockState.setValue(STATE_COLOR_ID, ColorUtils.getColor(useContext.getItemInHand()).getId());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        ItemStack stack = new ItemStack(ModItems.COLORED_SKULL.get(), 1);
        ColorUtils.setColor(stack, DyeColor.byId(ColorUtils.getColorId(state)));
        drops.add(stack);
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(STATE_COLOR_ID);
    }

}
