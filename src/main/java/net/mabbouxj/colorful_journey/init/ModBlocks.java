package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulJourney.MOD_ID);

    public static final List<RegistryObject<Block>> ALL_COLORED_VARIANTS_BLOCKS = new ArrayList<>();
    public static Map<DyeColor, RegistryObject<Block>> COLORED_SKULLS = registerColoredVariantsBlock("colored_skull", ColoredSkullBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_WALL_SKULLS = registerColoredVariantsBlock("colored_wall_skull", ColoredWallSkullBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_LOGS = registerColoredVariantsBlock("colored_log", ColoredLogBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_LEAVES = registerColoredVariantsBlock("colored_leaves",ColoredLeavesBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_SAPLINGS = registerColoredVariantsBlock("colored_sapling", ColoredSaplingBlock::new);

    private static Map<DyeColor, RegistryObject<Block>> registerColoredVariantsBlock(String name, Function<DyeColor, ? extends Block> supplier) {
        Map<DyeColor, RegistryObject<Block>> map = new HashMap<>();
        for (DyeColor color: ColorfulJourney.COLORS) {
            RegistryObject<Block> registryObject = BLOCKS.register(name + "_" + color.getName(), () -> supplier.apply(color));
            ALL_COLORED_VARIANTS_BLOCKS.add(registryObject);
            map.put(color, registryObject);
        }
        return map;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

}
