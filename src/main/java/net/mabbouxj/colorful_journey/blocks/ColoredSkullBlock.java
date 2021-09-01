package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.entities.ColoredWitherEntity;
import net.mabbouxj.colorful_journey.init.ModEntityTypes;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ColoredSkullBlock extends WitherSkeletonSkullBlock implements IColoredBlock {

    @Nullable
    private static BlockPattern witherPatternFull;
    private final DyeColor color;

    public ColoredSkullBlock(DyeColor color) {
        super(AbstractBlock.Properties.of(Material.DECORATION).strength(1.0F));
        this.color = color;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, blockPos, blockState, entity, itemStack);
        checkSpawn(world, blockPos, blockState);
    }

    public static void checkSpawn(World world, BlockPos blockPos, BlockState blockState) {
        if (!world.isClientSide) {
            boolean flag = blockState.getBlock() instanceof WitherSkeletonSkullBlock || blockState.getBlock() instanceof WitherSkeletonWallSkullBlock;
            if (flag && blockPos.getY() >= 0 && world.getDifficulty() != Difficulty.PEACEFUL) {
                BlockPattern blockpattern = getOrCreateWitherFull();
                BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.find(world, blockPos);
                if (blockpattern$patternhelper != null) {
                    for (int i = 0; i < blockpattern.getWidth(); ++i) {
                        for (int j = 0; j < blockpattern.getHeight(); ++j) {
                            CachedBlockInfo cachedblockinfo = blockpattern$patternhelper.getBlock(i, j, 0);
                            world.setBlock(cachedblockinfo.getPos(), Blocks.AIR.defaultBlockState(), 2);
                            world.levelEvent(2001, cachedblockinfo.getPos(), Block.getId(cachedblockinfo.getState()));
                        }
                    }

                    ColoredWitherEntity coloredWitherEntity = ModEntityTypes.COLORED_WITHER.get().create(world);
                    coloredWitherEntity.setColor(ColorUtils.getColor(blockState));
                    BlockPos blockpos = blockpattern$patternhelper.getBlock(1, 2, 0).getPos();
                    coloredWitherEntity.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.55D, (double) blockpos.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                    coloredWitherEntity.yBodyRot = blockpattern$patternhelper.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                    coloredWitherEntity.makeInvulnerable();

                    for (ServerPlayerEntity serverplayerentity : world.getEntitiesOfClass(ServerPlayerEntity.class, coloredWitherEntity.getBoundingBox().inflate(50.0D))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, coloredWitherEntity);
                    }

                    world.addFreshEntity(coloredWitherEntity);

                    for (int k = 0; k < blockpattern.getWidth(); ++k) {
                        for (int l = 0; l < blockpattern.getHeight(); ++l) {
                            world.blockUpdated(blockpattern$patternhelper.getBlock(k, l, 0).getPos(), Blocks.AIR);
                        }
                    }

                }
            }
        }
    }

    private static BlockPattern getOrCreateWitherFull() {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "###", "~#~")
                    .where('#', (blockInfo) -> {
                        return blockInfo.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS);
                    }).where('^', CachedBlockInfo.hasState( (blockState) -> {
                        return blockState.getBlock() instanceof WitherSkeletonSkullBlock || blockState.getBlock() instanceof WitherSkeletonWallSkullBlock;
                    }))
                    .where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return witherPatternFull;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
