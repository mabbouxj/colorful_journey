package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ColorfulJourneyItemGroup extends ItemGroup {

    public ColorfulJourneyItemGroup() {
        super(ColorfulJourney.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.INK_BALL.get());
    }

}
