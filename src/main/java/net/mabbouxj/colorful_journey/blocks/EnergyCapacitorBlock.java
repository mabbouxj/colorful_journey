package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.items.EnergyCapacitorBlockItem;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyCapacitorBlock extends ContainerBlock {

    public EnergyCapacitorBlock() {
        super(AbstractBlock.Properties
                .of(Material.METAL)
                .sound(SoundType.NETHERITE_BLOCK)
                .strength(2.5f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new EnergyCapacitorTile();
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
        if (! (te instanceof EnergyCapacitorTile))
            return ActionResultType.FAIL;

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, blockPos);
        return ActionResultType.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        TileEntity te = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        List<ItemStack> drops = super.getDrops(blockState, builder);
        if (te instanceof EnergyCapacitorTile) {
            EnergyCapacitorTile tileEntity = (EnergyCapacitorTile) te;
            drops.stream()
                    .filter(e -> e.getItem() instanceof EnergyCapacitorBlockItem)
                    .findFirst()
                    .ifPresent(e -> e.getOrCreateTag().putInt("energy", tileEntity.energyStorage.getEnergyStored()));
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
