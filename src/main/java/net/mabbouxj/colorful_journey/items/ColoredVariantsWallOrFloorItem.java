package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredVariantsWallOrFloorItem extends WallOrFloorItem implements ColorUtils.IColored {

    public final String registryName;
    private final DyeColor color;

    public ColoredVariantsWallOrFloorItem(Block floorBlock, Block wallBlock, String registryName, DyeColor color) {
        super(floorBlock, wallBlock, new Item.Properties()
                .tab(ColorfulJourney.ENABLED_COLORS.contains(color) ? ColorfulJourney.MOD_ITEM_GROUP: null)
                .stacksTo(64));
        this.registryName = registryName;
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public ITextComponent getName(ItemStack itemStack) {
        return ColorUtils.getDisplayItemColorName(itemStack, new TranslationTextComponent("block.colorful_journey." + this.registryName).getString());
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
