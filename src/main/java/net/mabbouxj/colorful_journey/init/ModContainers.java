package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<ContainerType<EnergyDyeGeneratorContainer>> ENERGY_DYE_GENERATOR = CONTAINERS.register("energy_dye_generator", () -> IForgeContainerType.create(EnergyDyeGeneratorContainer::new));

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

}
