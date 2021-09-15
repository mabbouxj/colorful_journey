package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.ColoredVariantsItemEntity;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ColoredEnderPearlItem extends EnderPearlItem implements ColorUtils.IColored {

    private final DyeColor color;

    public ColoredEnderPearlItem(DyeColor color) {
        super(new Item.Properties()
            .tab(ColorfulJourney.ENABLED_COLORS.contains(color) ? ColorfulJourney.MOD_ITEM_GROUP: null)
            .stacksTo(64));
        this.color = color;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new ColoredVariantsItemEntity(world, location, itemstack);
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
