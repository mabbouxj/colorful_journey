package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.tiles.EaselTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EaselBlock extends ContainerBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 25.0D, 13.0D);

    public EaselBlock() {
        super(AbstractBlock.Properties
                .of(Material.WOOD)
                .sound(SoundType.WOOD)
                .strength(1.5f)
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

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        // Remove paper from block
        if (tile.hasSlate && player.isCrouching()) {
            if (tile.popSlate(world, blockPos)) {
                world.playSound(null, blockPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
                tile.setChanged();
                return ActionResultType.SUCCESS;
            }
        }

        // Add paper to block
        if (!tile.hasSlate && item.equals(Items.PAPER)) {
            player.getItemInHand(hand).shrink(1);
            world.playSound(null, blockPos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
            tile.hasSlate = true;
            tile.setChanged();
            return ActionResultType.SUCCESS;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, blockPos);
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

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(newState.getBlock() != this) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null) {
                LazyOptional<IItemHandler> cap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                cap.ifPresent(handler -> {
                    for( int i = 0; i < handler.getSlots(); i ++ )
                        InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
                });
                if (tileEntity instanceof EaselTile) {
                    ((EaselTile) tileEntity).popSlate(world, pos);
                }
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

}
