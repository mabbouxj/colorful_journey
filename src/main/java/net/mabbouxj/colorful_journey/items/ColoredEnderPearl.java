package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ColoredEnderPearl extends EnderPearlItem implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredEnderPearl(DyeColor color) {
        super(new Item.Properties()
            .tab(ColorfulJourney.ENABLED_COLORS.contains(color) ? ColorfulJourney.MOD_ITEM_GROUP: null)
            .stacksTo(64));
        this.color = color;
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        return ColorUtils.getDisplayItemColorName(itemStack, new TranslationTextComponent("item.colorful_journey.colored_ender_pearl").getString());
    }

    @Override
    public DyeColor getColor() {
        return color;
    }
}
