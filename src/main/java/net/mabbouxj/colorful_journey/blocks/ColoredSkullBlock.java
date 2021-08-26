package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredSkullBlock extends SkullBlock {

    public ColoredSkullBlock(ISkullType skullType) {
        super(skullType, AbstractBlock.Properties.of(Material.DECORATION).strength(1.0F));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(ROTATION, 0)
                .setValue(STATE_COLOR_ID, DyeColor.WHITE.getId())
        );
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
        return defaultBlockState()
                .setValue(ROTATION, MathHelper.floor((double) (useContext.getRotation() * 16.0F / 360.0F) + 0.5D) & 15)
                .setValue(STATE_COLOR_ID, ColorUtils.getColor(useContext.getItemInHand()).getId());
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
        stateBuilder.add(ROTATION);
        stateBuilder.add(STATE_COLOR_ID);
    }

}
