package net.mabbouxj.colorful_journey.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Reference.MOD_ID, "jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(
                ModItems.COLORED_FEATHER.get(),
                ModItems.COLORED_EGG.get(),
                ModItems.COLORED_HONEYCOMB.get()
        );
    }
}
