package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.mabbouxj.colorful_journey.utils.StringUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyDyeGeneratorBlockItem extends BlockItem {

    public EnergyDyeGeneratorBlockItem() {
        super(ModBlocks.ENERGY_DYE_GENERATOR.get(),
                new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltips, flag);
        int power = stack.getOrCreateTag().getInt("energy");
        if( power == 0 )
            return;
        tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(power), StringUtils.numberWithSuffix(ModConfigs.COMMON_CONFIG.ENERGY_DYE_GENERATOR_BUFFER_CAPACITY.get())).withStyle(TextFormatting.GREEN));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof EnergyDyeGeneratorTile) {
            EnergyDyeGeneratorTile tile = (EnergyDyeGeneratorTile) te;
            tile.energyStorage.receiveEnergy(stack.getOrCreateTag().getInt("energy"), false);
        }
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }
}
