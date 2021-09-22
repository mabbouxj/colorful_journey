package net.mabbouxj.colorful_journey.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEnergyStorageCapability implements IEnergyStorage, INBTSerializable<CompoundNBT> {

    private static final String KEY = "energy";
    private int energy = 0;
    private final int capacity;
    private final int maxInOut;
    private final TileEntity tile;
    private boolean canExtract = true;
    private boolean canReceive = true;

    public TileEnergyStorageCapability(TileEntity tile, int capacity, int maxInOut) {
        this.capacity = capacity;
        this.tile = tile;
        this.maxInOut = maxInOut;
    }

    public TileEnergyStorageCapability(TileEntity tile, int capacity, int maxInOut, boolean canExtract, boolean canReceive) {
        this(tile, capacity, maxInOut);
        this.canExtract = canExtract;
        this.canReceive = canReceive;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt(KEY, this.energy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt(KEY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInOut, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            this.tile.setChanged();
        }

        return energyReceived;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    // We don't use this method and thus we don't let other people use it either
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxInOut, maxExtract));

        if (!simulate)
            energy -= energyExtracted;

        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return canExtract;
    }

    @Override
    public boolean canReceive() {
        return canReceive;
    }

    public int getMaxInOut() {
        return maxInOut;
    }

    @Override
    public String toString() {
        return "ChargerEnergyStorage{" +
                "energy=" + energy +
                ", capacity=" + capacity +
                ", maxInOut=" + maxInOut +
                '}';
    }

}
