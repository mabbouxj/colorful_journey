package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulJourney.MOD_ID);

    public static RegistryObject<Block> RUBIKS_CUBE = BLOCKS.register("rubiks_cube", RubiksCubeBlock::new);
    public static RegistryObject<Block> RUBIKS_CUBE_UNFINISHED = BLOCKS.register("rubiks_cube_unfinished", RubiksCubeUnfinishedBlock::new);

    public static final List<RegistryObject<Block>> ALL_COLORED_VARIANTS_BLOCKS = new ArrayList<>();
    public static Map<DyeColor, RegistryObject<Block>> COLORED_SKULLS = registerColoredVariantsBlock("colored_skull", ColoredSkullBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_WALL_SKULLS = registerColoredVariantsBlock("colored_wall_skull", ColoredWallSkullBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_LOGS = registerColoredVariantsBlock("colored_log", ColoredRotatedPillarBlock::new, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_STRIPPED_LOGS = registerColoredVariantsBlock("colored_stripped_log", ColoredRotatedPillarBlock::new, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_WOODS = registerColoredVariantsBlock("colored_wood", ColoredRotatedPillarBlock::new, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_STRIPPED_WOODS = registerColoredVariantsBlock("colored_stripped_wood", ColoredRotatedPillarBlock::new, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_LEAVES = registerColoredVariantsBlock("colored_leaves",ColoredLeavesBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_SAPLINGS = registerColoredVariantsBlock("colored_sapling", ColoredSaplingBlock::new);
    public static Map<DyeColor, RegistryObject<Block>> COLORED_PLANKS = registerColoredVariantsBlock("colored_plank", ColoredBlock::new, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_ORES = registerColoredVariantsBlock("colored_ore", ColoredBlock::new, AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    public static Map<DyeColor, RegistryObject<Block>> COLORED_INGOT_BLOCKS = registerColoredVariantsBlock("colored_ingot_block", ColoredBlock::new, AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));

    private static Map<DyeColor, RegistryObject<Block>> registerColoredVariantsBlock(String name, Function<DyeColor, ? extends Block> supplier) {
        Map<DyeColor, RegistryObject<Block>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<Block> registryObject = BLOCKS.register(name + "_" + color.getName(), () -> supplier.apply(color));
            ALL_COLORED_VARIANTS_BLOCKS.add(registryObject);
            map.put(color, registryObject);
        }
        return map;
    }

    private static Map<DyeColor, RegistryObject<Block>> registerColoredVariantsBlock(String name, BiFunction<AbstractBlock.Properties, DyeColor, ? extends Block> supplier, AbstractBlock.Properties props) {
        Map<DyeColor, RegistryObject<Block>> map = new HashMap<>();
        for (DyeColor color: DyeColor.values()) {
            RegistryObject<Block> registryObject = BLOCKS.register(name + "_" + color.getName(), () -> supplier.apply(props, color));
            ALL_COLORED_VARIANTS_BLOCKS.add(registryObject);
            map.put(color, registryObject);
        }
        return map;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

}
