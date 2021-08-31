package net.mabbouxj.colorful_journey.init;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.ColoredSkullBlock;
import net.mabbouxj.colorful_journey.blocks.ColoredWallSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulJourney.MOD_ID);

    public static RegistryObject<Block> COLORED_SKULL = BLOCKS.register("colored_skull", () -> new ColoredSkullBlock(SkullBlock.Types.WITHER_SKELETON));
    public static RegistryObject<Block> COLORED_WALL_SKULL = BLOCKS.register("colored_wall_skull", () -> new ColoredWallSkullBlock(SkullBlock.Types.WITHER_SKELETON));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

}
