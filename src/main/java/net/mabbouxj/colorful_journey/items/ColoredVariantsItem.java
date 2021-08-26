package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

public class ColoredVariantsItem extends Item {

    private final String registryName;

    public ColoredVariantsItem(Properties properties, String registryName) {
        super(properties);
        this.registryName = registryName;
    }

    @Override
    public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> subItems) {
        if (this.getItemCategory() == itemGroup) {
            for (DyeColor color : ColorfulJourney.COLORS) {
                ItemStack subItemStack = new ItemStack(this, 1);
                ColorUtils.setColor(subItemStack, color);
                subItems.add(subItemStack);
            }
        }
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        String colorName = StringUtils.capitalize(ColorUtils.getColor(itemStack).getName());
        String itemName = new TranslationTextComponent("item.colorful_journey." + this.registryName).getString();
        return new StringTextComponent(colorName + " " + itemName);
    }

}
