package net.mabbouxj.colorful_journey;

import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ColorfulJourney.MOD_ID)
public class ModConfigs {

    private static final String config = "config." + ColorfulJourney.MOD_ID + ".";
    public static Common COMMON;
    public static Client CLIENT;

    public static class Common {

        public ForgeConfigSpec.ConfigValue<List<String>> ENABLED_COLORS;
        public ForgeConfigSpec.IntValue LASER_GUN_BUFFER_CAPACITY;
        public ForgeConfigSpec.IntValue LASER_GUN_ENERGY_COST_PER_TICK;
        public ForgeConfigSpec.IntValue LASER_GUN_ENERGY_COST_PER_TRANSFORMATION;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_BUFFER_CAPACITY;
        public ForgeConfigSpec.DoubleValue ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_MAX_IN_OUT;
        public ForgeConfigSpec.IntValue ENERGY_CAPACITOR_BUFFER_CAPACITY;
        public ForgeConfigSpec.IntValue ENERGY_CAPACITOR_MAX_IN_OUT;
        public ForgeConfigSpec.IntValue WASHING_MACHINE_ENERGY_BUFFER;
        public ForgeConfigSpec.IntValue WASHING_MACHINE_ENERGY_CONSUMPTION;
        public ForgeConfigSpec.IntValue WASHING_MACHINE_FLUID_BUFFER;

        public Common(ForgeConfigSpec.Builder builder) {

            ENABLED_COLORS = builder
                    .comment("To enable colored variants items/blocks. Possible values are the 16 Minecraft Vanilla dye color names. If empty, this mod is useless ;)")
                    .define("enabled_colors", Arrays.stream(DyeColor.values()).map(DyeColor::getName).collect(Collectors.toList()));

            LASER_GUN_BUFFER_CAPACITY = builder
                    .comment("Buffer capacity (in FE) of the Laser gun")
                    .defineInRange("laser_gun_buffer_capacity", 100000, 1000, 1000000000);
            LASER_GUN_ENERGY_COST_PER_TICK = builder
                    .comment("Energy cost (in FE/tick) while using the Laser gun")
                    .defineInRange("laser_gun_cost_per_tick", 100, 0, 1000000);
            LASER_GUN_ENERGY_COST_PER_TRANSFORMATION = builder
                    .comment("Energy cost (in FE) when the Laser gun transforms something")
                    .defineInRange("laser_gun_cost_per_transformation", 1000, 0, 1000000);

            ENERGY_DYE_GENERATOR_BUFFER_CAPACITY = builder
                    .comment("Buffer capacity (in FE) of the Energy dye generator")
                    .defineInRange("energy_dye_generator_buffer_capacity", 100000, 1000, 1000000000);
            ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED = builder
                    .comment("How fast the fuel should be burn. Setting it to 10.0 will consume fuel 10x faster")
                    .defineInRange("energy_dye_generator_fuel_consumption_speed", 10, 0.001, 1000);
            ENERGY_DYE_GENERATOR_MAX_IN_OUT = builder
                    .comment("Maximum IN/OUT energy flow (in FE) of the Energy dye generator")
                    .defineInRange("energy_dye_generator_max_in_out", 1000, 1, 1000000000);

            ENERGY_CAPACITOR_BUFFER_CAPACITY = builder
                    .comment("Buffer capacity (in FE) of the Energy capacitor")
                    .defineInRange("energy_capacitor_buffer_capacity", 1000000, 1000, 1000000000);
            ENERGY_CAPACITOR_MAX_IN_OUT = builder
                    .comment("Maximum IN/OUT energy flow (in FE) of the Energy capacitor")
                    .defineInRange("energy_capacitor_max_in_out", 10000, 1, 1000000000);

            WASHING_MACHINE_ENERGY_BUFFER = builder
                    .comment("Energy buffer capacity (in FE) of the Washing machine")
                    .defineInRange("washing_machine_energy_buffer", 10000, 1000, 1000000);
            WASHING_MACHINE_FLUID_BUFFER = builder
                    .comment("Fluid buffer capacity (in mB) of the Washing machine")
                    .defineInRange("washing_machine_fluid_buffer", 10000, 1000, 1000000);

        }

    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {

        }

    }

    public static void build() {

    }

}
