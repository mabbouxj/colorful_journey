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
    public static Common COMMON_CONFIG;
    public static Client CLIENT_CONFIG;

    public static class Common {

        public ForgeConfigSpec.ConfigValue<List<String>> ENABLED_COLORS;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_BUFFER_CAPACITY;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_PRODUCTION_PER_TICK;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_PRODUCTION_PER_MATERIAL;
        public ForgeConfigSpec.DoubleValue ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED;
        public ForgeConfigSpec.IntValue ENERGY_DYE_GENERATOR_MAX_IN_OUT;

        public Common(ForgeConfigSpec.Builder builder) {

            ENABLED_COLORS = builder
                    .comment("To enable colored variants items/blocks. Possible values are the 16 Minecraft Vanilla dye color names. If empty, this mod is useless ;)")
                    .define("enabled_colors", Arrays.stream(DyeColor.values()).map(DyeColor::getName).collect(Collectors.toList()));

            ENERGY_DYE_GENERATOR_BUFFER_CAPACITY = builder
                    .comment("Buffer capacity (in FE) of the Energy dye generator")
                    .defineInRange("energy_dye_generator_buffer_capacity", 100000, 1000, 1000000000);

            ENERGY_DYE_GENERATOR_PRODUCTION_PER_TICK = builder
                    .comment("Amount of energy generated per tick (while ignited) by the Energy dye generator")
                    .defineInRange("energy_dye_generator_production_per_tick", 20, 1, 1000000000);

            ENERGY_DYE_GENERATOR_PRODUCTION_PER_MATERIAL = builder
                    .comment("Total amount of energy generated per material (dye being burnt)")
                    .defineInRange("energy_dye_generator_production_per_material", 1000, 1, 1000000000);

            ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED = builder
                    .comment("How fast the fuel should be burn. Setting it to 10.0 will consume fuel 10x faster")
                    .defineInRange("energy_dye_generator_fuel_consumption_speed", 10, 0.001, 1000);

            ENERGY_DYE_GENERATOR_MAX_IN_OUT = builder
                    .comment("Maximum IN/OUT energy flow (in FE)")
                    .defineInRange("energy_dye_generator_max_in_out", 1000, 1, 1000000000);

        }

    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {

        }

    }

    public static void build() {

    }

}
