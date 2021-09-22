package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.capabilities.ItemEnergyStorageCapability;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyCapacitorBlockItem extends BlockItem {

    private final int energyCapacity = ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_BUFFER_CAPACITY.get();

    public EnergyCapacitorBlockItem() {
        super(ModBlocks.ENERGY_CAPACITOR.get(), new Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.energyCapacity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null)
                .map(e -> 1D - (e.getEnergyStored() / (double) e.getMaxEnergyStored()))
                .orElse(0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map(e -> MathHelper.hsvToRgb(Math.max(0.0F, (float) e.getEnergyStored() / (float) e.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F))
                .orElse(super.getRGBDurabilityForDisplay(stack));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyStorageCapability(stack, energyCapacity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltips, flag);
        int power = stack.getOrCreateTag().getInt("energy");
        if( power == 0 )
            return;
        tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(power), StringUtils.numberWithSuffix(ModConfigs.COMMON_CONFIG.ENERGY_CAPACITOR_BUFFER_CAPACITY.get())).withStyle(TextFormatting.GREEN));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof EnergyCapacitorTile) {
            ((EnergyCapacitorTile) te).energyStorage.setEnergy(stack.getOrCreateTag().getInt("energy"));
        }
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }
}
