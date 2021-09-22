package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.items.WashingMachineBlockItem;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class WashingMachineBlock extends ContainerBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public WashingMachineBlock() {
        super(AbstractBlock.Properties
                .of(Material.METAL)
                .sound(SoundType.NETHERITE_BLOCK)
                .strength(2.5f)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new WashingMachineTile();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {
        return newBlockEntity(blockReader);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        TileEntity te = world.getBlockEntity(blockPos);
        if (! (te instanceof WashingMachineTile))
            return ActionResultType.FAIL;

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (item == Items.WATER_BUCKET) {
            int usedFluid = ((WashingMachineTile) te).fluidStorage.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            if (usedFluid > 0) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                world.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
        } else if (item == Items.LAVA_BUCKET) {
            int usedFluid = ((WashingMachineTile) te).fluidStorage.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.EXECUTE);
            if (usedFluid > 0) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                world.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
        } else if (item == Items.BUCKET) {
            FluidStack drainedFluid = ((WashingMachineTile) te).fluidStorage.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (drainedFluid.getAmount() == 1000 && drainedFluid.getFluid().isSame(Fluids.WATER)) {
                ((WashingMachineTile) te).fluidStorage.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                player.getItemInHand(hand).shrink(1);
                player.inventory.add(new ItemStack(Items.WATER_BUCKET));
                world.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            } else if (drainedFluid.getAmount() == 1000 && drainedFluid.getFluid().isSame(Fluids.LAVA)) {
                ((WashingMachineTile) te).fluidStorage.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                player.getItemInHand(hand).shrink(1);
                player.inventory.add(new ItemStack(Items.LAVA_BUCKET));
                world.playSound(null, blockPos, SoundEvents.BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, blockPos);
        return ActionResultType.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        TileEntity te = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        List<ItemStack> drops = super.getDrops(blockState, builder);
        if (te instanceof WashingMachineTile) {
            WashingMachineTile tileEntity = (WashingMachineTile) te;
            drops.stream()
                    .filter(e -> e.getItem() instanceof WashingMachineBlockItem)
                    .findFirst()
                    .ifPresent(e -> {
                        e.getOrCreateTag().putInt("energy", tileEntity.energyStorage.getEnergyStored());
                        e.getOrCreateTag().put("fluid", tileEntity.fluidStorage.serializeNBT());
                    });
        }
        return drops;
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
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

}
