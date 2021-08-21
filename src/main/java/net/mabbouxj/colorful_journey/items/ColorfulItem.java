package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.Reference;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

public class ColorfulItem extends Item {

    public static final String NBT_TAG_NAME_COLOR = "color";
    private String registryName;

    public ColorfulItem(Properties properties, String registryName) {
        super(properties);
        this.registryName = registryName;
    }

    public static DyeColor getColor(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains(NBT_TAG_NAME_COLOR)) {
            return DyeColor.byId(nbt.getInt(NBT_TAG_NAME_COLOR));
        }
        return DyeColor.WHITE;
    }

    public static void setColor(ItemStack stack, DyeColor color) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt(NBT_TAG_NAME_COLOR, color.getId());
    }

    @Override
    public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> subItems) {
        if (this.getItemCategory() == itemGroup) {
            for (DyeColor color : Reference.COLORS) {
                ItemStack subItemStack = new ItemStack(this, 1);
                setColor(subItemStack, color);
                subItems.add(subItemStack);
            }
        }
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        String colorName = StringUtils.capitalize(getColor(itemStack).getName());
        String itemName = new TranslationTextComponent("item.colorful_journey." + this.registryName).getString();
        return new StringTextComponent(colorName + " " + itemName);
    }

}
