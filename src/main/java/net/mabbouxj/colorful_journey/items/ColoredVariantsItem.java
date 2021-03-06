package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.ColoredVariantsItemEntity;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class ColoredVariantsItem extends Item implements ColorUtils.IColored {

    private final String registryName;
    private final DyeColor color;
    private final RegistryObject<Item> initialItem;

    public ColoredVariantsItem(String registryName, DyeColor color, RegistryObject<Item> initialItem) {
        super(new Item.Properties()
                .tab(ColorfulJourney.ENABLED_COLORS.contains(color) ? ColorfulJourney.MOD_ITEM_GROUP: null)
                .stacksTo(64));
        this.registryName = registryName;
        this.color = color;
        this.initialItem = initialItem;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public Item getInitialItem() {
        if (initialItem != null)
            return initialItem.get();
        return null;
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
        return ColorUtils.getDisplayItemColorName(itemStack, "item.colorful_journey." + this.registryName);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return this.registryName.contains("nether_star");
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
