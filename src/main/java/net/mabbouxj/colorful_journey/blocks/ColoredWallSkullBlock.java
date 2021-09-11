package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.WitherSkeletonWallSkullBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ColoredWallSkullBlock extends WitherSkeletonWallSkullBlock implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredWallSkullBlock(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.DECORATION).strength(1.0F));
        this.color = color;
    }

    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack stack) {
        ModBlocks.COLORED_SKULLS.get(color).get().setPlacedBy(world, blockPos, blockState, entity, stack);
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
    public DyeColor getColor() {
        return color;
    }
}
