package net.mabbouxj.colorful_journey.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class TileFluidStorageCapability implements IFluidHandler, INBTSerializable<CompoundNBT> {

    private FluidStack fluid = FluidStack.EMPTY;
    private final int capacity;
    private final TileEntity tile;

    public TileFluidStorageCapability(TileEntity tile, int capacity) {
        this.tile = tile;
        this.capacity = capacity;
    }

    public void setFluidStack(FluidStack stack) {
        this.fluid = stack.isEmpty() ? FluidStack.EMPTY: stack;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        fluid.writeToNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setFluidStack(FluidStack.loadFluidStackFromNBT(nbt));
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluid;
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (fluid.isEmpty()) return true;
        return fluid.isFluidEqual(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || fluid.getAmount() == capacity) {
            return 0;
        }

        int maxCanFill = 0;
        if (fluid.isEmpty() || fluid.isFluidEqual(resource)) {
            maxCanFill = Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }

        if (maxCanFill > 0 && action.execute()) {
            setFluidStack(new FluidStack(resource.getFluid(), fluid.getAmount() + maxCanFill));
            tile.setChanged();
        }

        return maxCanFill;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0 || fluid.isEmpty()) {
            return FluidStack.EMPTY;
        }
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(fluid, drained);
        if (action.execute()) {
            fluid.shrink(drained);
            if (fluid.isEmpty()) {
                setFluidStack(FluidStack.EMPTY);
            }
        }
        return stack;
    }

}
