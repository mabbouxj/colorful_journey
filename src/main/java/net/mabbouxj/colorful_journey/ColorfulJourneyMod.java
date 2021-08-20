package net.mabbouxj.colorful_journey;

import net.mabbouxj.colorful_journey.init.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;


@Mod(Reference.MOD_ID)
public class ColorfulJourneyMod
{

    public ColorfulJourneyMod() {

        Registration.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

}
