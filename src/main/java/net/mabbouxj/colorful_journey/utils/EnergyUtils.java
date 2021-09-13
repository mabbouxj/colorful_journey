package net.mabbouxj.colorful_journey.utils;

import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Arrays;

public class EnergyUtils {

    public static void extractToAdjacentBlocks(World world, TileEnergyStorageCapability energyStorage, BlockPos pos) {
        for (BlockPos p: Arrays.asList(pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west())) {
            if (world == null)
                continue;
            TileEntity t = world.getBlockEntity(p);
            if (t != null) {
                t.getCapability(CapabilityEnergy.ENERGY).ifPresent(adjacentEnergyStorage -> {
                    if (!adjacentEnergyStorage.canReceive()) {
                        return;
                    }
                    boolean canOutputEnergy = adjacentEnergyStorage.receiveEnergy(energyStorage.getMaxInOut(), true) > 0;
                    if (canOutputEnergy) {
                        int energyRemoved = adjacentEnergyStorage.receiveEnergy(Math.min(energyStorage.getEnergyStored(), energyStorage.getMaxInOut()), false);
                        energyStorage.extractEnergy(energyRemoved, false);
                    }
                });
            }
        }
    }

    public static void extractFromAdjacentBlocks(World world, TileEnergyStorageCapability energyStorage, BlockPos pos) {
        for (BlockPos p: Arrays.asList(pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west())) {
            if (world == null)
                continue;
            TileEntity t = world.getBlockEntity(p);
            if (t != null) {
                if (t instanceof EnergyDyeGeneratorTile || t instanceof EnergyCapacitorTile) {
                    // Skip: they already output energy to adjacent bocks
                    continue;
                }
                t.getCapability(CapabilityEnergy.ENERGY).ifPresent(adjacentEnergyStorage -> {
                    if (!adjacentEnergyStorage.canExtract()) {
                        return;
                    }
                    boolean canInputEnergy = energyStorage.receiveEnergy(energyStorage.getMaxInOut(), true) > 0;
                    if (canInputEnergy) {
                        int energyRemoved = energyStorage.receiveEnergy(Math.min(adjacentEnergyStorage.getEnergyStored(), energyStorage.getMaxInOut()), false);
                        adjacentEnergyStorage.extractEnergy(energyRemoved, false);
                    }
                });
            }
        }
    }

}
