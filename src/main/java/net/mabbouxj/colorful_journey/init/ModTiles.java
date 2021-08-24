package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModTiles {

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ColorfulJourney.MOD_ID);

    public static void register(IEventBus bus) {
        TILE_ENTITIES.register(bus);
    }

}
