package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.utils.nbthandler.NBTManager;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicTile extends TileEntity {

    public BasicTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        NBTManager.getInstance().readTileEntity(this, nbt);
        super.load(state, nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        return NBTManager.getInstance().writeTileEntity(this, super.save(nbt));
    }

    public void markForUpdate() {
        if (this.level != null)
            this.level.sendBlockUpdated(getBlockPos(), this.level.getBlockState(getBlockPos()), this.level.getBlockState(getBlockPos()), 3);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        save(nbt);
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        load(getBlockState(), pkt.getTag());
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState stateIn, CompoundNBT tag) {
        load(stateIn, tag);
    }

    @Override
    public void setChanged() {
        markForUpdate();
        super.setChanged();
    }
}
