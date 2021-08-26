package net.mabbouxj.colorful_journey.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

@JeiPlugin
public class JeiCompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ColorfulJourney.MOD_ID, "jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (RegistryObject<Item> registryItem : ModItems.COLORED_VARIANTS_ITEMS) {
            registration.useNbtForSubtypes(registryItem.get());
        }
        for (RegistryObject<BlockItem> registryItem : ModItems.COLORED_VARIANTS_BLOCK_ITEMS) {
            registration.useNbtForSubtypes(registryItem.get());
        }
    }
}
