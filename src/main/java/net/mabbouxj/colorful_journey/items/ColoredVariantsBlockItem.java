package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredVariantsBlockItem extends BlockItem {

    public final String registryName;
    private final DyeColor color;

    public ColoredVariantsBlockItem(Block block, String registryName, DyeColor color) {
        super(block, new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
        this.registryName = registryName;
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        DyeColor color = ColorUtils.getColor(itemStack);
        String colorName = StringUtils.capitalize(color.getName());
        String itemName = new TranslationTextComponent("block.colorful_journey." + this.registryName).getString();
        return new StringTextComponent(ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + colorName + " " + itemName);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        TranslationTextComponent tooltip = new TranslationTextComponent("tooltip.colorful_journey." + this.registryName);
        if (!tooltip.getString().contains("tooltip.colorful_journey")) {
            if (Screen.hasShiftDown()) {
                tooltips.add(tooltip);
            } else {
                tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.hold_shift_for_info"));
            }
        }
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
