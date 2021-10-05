package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.world.gen.OreGeneration;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredVariantsBlockItem extends BlockItem implements ColorUtils.IColored {

    public final String registryName;
    private final DyeColor color;

    public ColoredVariantsBlockItem(Block block, String registryName, DyeColor color) {
        super(block, new Item.Properties()
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
        return ColorUtils.getDisplayItemColorName(itemStack, "block.colorful_journey." + this.registryName);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        TranslationTextComponent tooltip = new TranslationTextComponent("tooltip.colorful_journey." + this.registryName);
        if (!tooltip.getString().contains("tooltip.colorful_journey")) {
            if (this.registryName.equals("colored_ore")) {
                int minHeight = OreGeneration.OVERWORLD_COLORED_ORE_GEN_MIN_MAX.get(ColorUtils.getColor(itemStack)).getFirst();
                int maxHeight = OreGeneration.OVERWORLD_COLORED_ORE_GEN_MIN_MAX.get(ColorUtils.getColor(itemStack)).getSecond();
                tooltips.add(new StringTextComponent(tooltip.getString() + "[" + minHeight + ";" + maxHeight+ "]"));
            } else {
                tooltips.add(tooltip);
            }
        }
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
