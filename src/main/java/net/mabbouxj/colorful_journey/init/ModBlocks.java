package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulJourney.MOD_ID);


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

}
