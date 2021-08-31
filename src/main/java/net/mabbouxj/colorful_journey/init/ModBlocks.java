package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulJourney.MOD_ID);

    public static final List<RegistryObject<Block>> COLORED_VARIANTS_BLOCKS = new ArrayList<>();
    public static RegistryObject<Block> COLORED_SKULL = registerColoredVariantsBlock("colored_skull", () -> new ColoredSkullBlock(SkullBlock.Types.WITHER_SKELETON));
    public static RegistryObject<Block> COLORED_WALL_SKULL = registerColoredVariantsBlock("colored_wall_skull", () -> new ColoredWallSkullBlock(SkullBlock.Types.WITHER_SKELETON));
    public static RegistryObject<Block> COLORED_LOG = registerColoredVariantsBlock("colored_log", ColoredLogBlock::new);
    public static RegistryObject<Block> COLORED_LEAVES = registerColoredVariantsBlock("colored_leaves",ColoredLeavesBlock::new);
    public static RegistryObject<Block> COLORED_SAPLING = registerColoredVariantsBlock("colored_sapling", ColoredSaplingBlock::new);

    private static RegistryObject<Block> registerColoredVariantsBlock(String name, Supplier<? extends Block> supplier) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, supplier);
        COLORED_VARIANTS_BLOCKS.add(registryObject);
        return registryObject;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

}
