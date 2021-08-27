package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.mabbouxj.colorful_journey.utils.ColorUtils.STATE_COLOR_ID;

public class ColoredSkullBlock extends SkullBlock {

    @Nullable
    private static BlockPattern witherPatternFull;
    @Nullable
    private static BlockPattern witherPatternBase;

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

    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, blockPos, blockState, entity, itemStack);
        checkSpawn(world, blockPos, blockState);
    }

    public static void checkSpawn(World world, BlockPos blockPos, BlockState blockState) {
        if (!world.isClientSide) {
            boolean flag = blockState.is(ModBlocks.COLORED_SKULL.get());
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

                    WitherEntity witherentity = EntityType.WITHER.create(world);
                    BlockPos blockpos = blockpattern$patternhelper.getBlock(1, 2, 0).getPos();
                    witherentity.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.55D, (double) blockpos.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                    witherentity.yBodyRot = blockpattern$patternhelper.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                    witherentity.makeInvulnerable();

                    for (ServerPlayerEntity serverplayerentity : world.getEntitiesOfClass(ServerPlayerEntity.class, witherentity.getBoundingBox().inflate(50.0D))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, witherentity);
                    }

                    world.addFreshEntity(witherentity);

                    for (int k = 0; k < blockpattern.getWidth(); ++k) {
                        for (int l = 0; l < blockpattern.getHeight(); ++l) {
                            world.blockUpdated(blockpattern$patternhelper.getBlock(k, l, 0).getPos(), Blocks.AIR);
                        }
                    }

                }
            }
        }
    }

    public static boolean canSpawnMob(World world, BlockPos blockPos, ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.COLORED_SKULL.get() && blockPos.getY() >= 2 && world.getDifficulty() != Difficulty.PEACEFUL && !world.isClientSide) {
            return getOrCreateWitherBase().find(world, blockPos) != null;
        }
        return false;
    }

    private static BlockPattern getOrCreateWitherFull() {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', (blockInfo) -> {
                return blockInfo.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS);
            }).where('^', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.COLORED_SKULL.get()))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return witherPatternFull;
    }

    private static BlockPattern getOrCreateWitherBase() {
        if (witherPatternBase == null) {
            witherPatternBase = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', (p_235638_0_) -> {
                return p_235638_0_.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS);
            }).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return witherPatternBase;
    }

}
