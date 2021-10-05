package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.containers.EaselContainer;
import net.mabbouxj.colorful_journey.containers.EnergyCapacitorContainer;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.mabbouxj.colorful_journey.containers.WashingMachineContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ColorfulJourney.MOD_ID);

    public static final RegistryObject<ContainerType<EnergyDyeGeneratorContainer>> ENERGY_DYE_GENERATOR = CONTAINERS.register("energy_dye_generator", () -> IForgeContainerType.create(EnergyDyeGeneratorContainer::new));
    public static final RegistryObject<ContainerType<EnergyCapacitorContainer>> ENERGY_CAPACITOR = CONTAINERS.register("energy_capacitor", () -> IForgeContainerType.create(EnergyCapacitorContainer::new));
    public static final RegistryObject<ContainerType<WashingMachineContainer>> WASHING_MACHINE = CONTAINERS.register("washing_machine", () -> IForgeContainerType.create(WashingMachineContainer::new));
    public static final RegistryObject<ContainerType<EaselContainer>> EASEL = CONTAINERS.register("easel", () -> IForgeContainerType.create(EaselContainer::new));

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

}
