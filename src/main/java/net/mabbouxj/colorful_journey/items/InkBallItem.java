package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class InkBallItem extends Item {

    public InkBallItem() {
        super(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        DyeColor color = ColorUtils.getRandomDyeColor();
        String itemName = new TranslationTextComponent("item.colorful_journey.ink_ball").getString();
        return new StringTextComponent(ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + itemName);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.ink_ball"));
        } else {
            tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.hold_shift_for_info"));
        }
        super.appendHoverText(itemStack, world, tooltips, flag);
    }
}
