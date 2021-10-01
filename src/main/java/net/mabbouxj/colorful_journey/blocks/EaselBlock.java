package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.tiles.EaselTile;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.ParticleUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EaselBlock extends ContainerBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 26.0D, 16.0D);

    public EaselBlock() {
        super(AbstractBlock.Properties
                .of(Material.WOOD)
                .sound(SoundType.WOOD)
                .strength(2.5f)
                .noOcclusion()
                .isViewBlocking((x,y,z) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return newBlockEntity(world);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader blockReader) {
        return new EaselTile();
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        TileEntity te = world.getBlockEntity(blockPos);
        if (!(te instanceof EaselTile))
            return ActionResultType.FAIL;
        EaselTile tile = (EaselTile) te;

        if (tile.hasSlate && player.isCrouching()) {
            if (tile.popSlate(world, blockPos)) {
                world.playSound(null, blockPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
                tile.setChanged();
                return ActionResultType.SUCCESS;
            }
        }

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (!tile.hasSlate && item.equals(Items.PAPER)) {
            player.getItemInHand(hand).shrink(1);
            world.playSound(null, blockPos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
            tile.hasSlate = true;
        } else if (tile.hasSlate && item.equals(ModItems.PAINTBRUSH.get()) && ColorUtils.hasColor(stack)) {
            ParticleUtils.makeParticles(player.level, ColorUtils.getColor(stack), 8, 20, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            world.playSound(null, blockPos, ModSounds.INK_SPLASH.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            ColorUtils.removeColor(stack);
            tile.changePaint();
        }

        tile.setChanged();
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(ROTATION, MathHelper.floor((double) (context.getRotation() * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(ROTATION, rotation.rotate(blockState.getValue(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(ROTATION, mirror.mirror(blockState.getValue(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(ROTATION);
    }

}
