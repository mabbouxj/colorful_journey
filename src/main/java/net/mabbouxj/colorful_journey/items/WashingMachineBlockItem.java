package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class WashingMachineBlockItem extends BlockItem {

    public WashingMachineBlockItem() {
        super(ModBlocks.WASHING_MACHINE.get(),
                new Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        int power = stack.getOrCreateTag().getInt("energy");
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("fluid"));
        tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(power), StringUtils.numberWithSuffix(ModConfigs.COMMON.WASHING_MACHINE_ENERGY_BUFFER.get())));
        if (fluid.isEmpty()) {
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.fluid_empty"));
        } else {
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.fluid", fluid.getDisplayName()));
            tooltips.add(new TranslationTextComponent("screen.colorful_journey.fluid_amount", StringUtils.numberWithSuffix(fluid.getAmount()), StringUtils.numberWithSuffix(ModConfigs.COMMON.WASHING_MACHINE_FLUID_BUFFER.get())));
        }
        super.appendHoverText(stack, world, tooltips, flag);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof WashingMachineTile) {
            ((WashingMachineTile) te).energyStorage.setEnergy(stack.getOrCreateTag().getInt("energy"));
            ((WashingMachineTile) te).fluidStorage.setFluidStack(FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("fluid")));
        }
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }
}
