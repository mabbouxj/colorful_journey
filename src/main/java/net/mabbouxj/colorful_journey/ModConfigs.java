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

        public Common(ForgeConfigSpec.Builder builder) {

            ENABLED_COLORS = builder
                    .translation(config + "enabled_colors")
                    .comment(config + "enabled_colors.tooltip")
                    .define("enabled_colors", Arrays.stream(DyeColor.values()).map(DyeColor::getName).collect(Collectors.toList()));

        }

    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {

        }

    }

    public static void build() {

    }

}
