package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ColorPaletteItem extends Item {

    public ColorPaletteItem() {
        super(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(1).durability(64));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRgb(Math.max(0.0F, 1 - ((float) stack.getDamageValue() / (float) stack.getMaxDamage())) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.color_palette"));
    }
}
