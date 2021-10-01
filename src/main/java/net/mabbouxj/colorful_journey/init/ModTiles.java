package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.tiles.EaselTile;
import net.mabbouxj.colorful_journey.tiles.EnergyCapacitorTile;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTiles {

    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ColorfulJourney.MOD_ID);

    public static final RegistryObject<TileEntityType<EnergyDyeGeneratorTile>> ENERGY_DYE_GENERATOR = TILES.register("energy_dye_generator", () -> TileEntityType.Builder.of(EnergyDyeGeneratorTile::new, ModBlocks.ENERGY_DYE_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<EnergyCapacitorTile>> ENERGY_CAPACITOR = TILES.register("energy_capacitor", () -> TileEntityType.Builder.of(EnergyCapacitorTile::new, ModBlocks.ENERGY_CAPACITOR.get()).build(null));
    public static final RegistryObject<TileEntityType<WashingMachineTile>> WASHING_MACHINE = TILES.register("washing_machine", () -> TileEntityType.Builder.of(WashingMachineTile::new, ModBlocks.WASHING_MACHINE.get()).build(null));
    public static final RegistryObject<TileEntityType<EaselTile>> EASEL = TILES.register("easel", () -> TileEntityType.Builder.of(EaselTile::new, ModBlocks.EASEL.get()).build(null));

    public static void register(IEventBus bus) {
        TILES.register(bus);
    }

}
