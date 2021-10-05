package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BasicItem extends Item {

    private final String registryName;

    public BasicItem(String registryName) {
        this(registryName, new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    public BasicItem(String registryName, Item.Properties properties) {
        super(properties);
        this.registryName = registryName;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRgb(Math.max(0.0F, 1 - ((float) stack.getDamageValue() / (float) stack.getMaxDamage())) / 3.0F, 1.0F, 1.0F);
    }

    @Nonnull
    @Override
    public ITextComponent getName(@Nonnull ItemStack itemStack) {
        TranslationTextComponent itemTranslation = new TranslationTextComponent("item.colorful_journey." + registryName);
        if (registryName.equals("ink_ball")) {
            DyeColor color = ColorUtils.getRandomEnabledColor();
            return new StringTextComponent(ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + itemTranslation.getString());
        }
        return itemTranslation;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        TranslationTextComponent tooltip = new TranslationTextComponent("tooltip.colorful_journey." + this.registryName);
        if (!tooltip.getString().contains("tooltip.colorful_journey")) {
            tooltips.add(tooltip);
        }
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
