package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.item.BlockItem;

public class EaselBlockItem extends BlockItem {

    public EaselBlockItem() {
        super(ModBlocks.EASEL.get(), new Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }


}
