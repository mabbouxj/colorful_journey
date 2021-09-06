package net.mabbouxj.colorful_journey.data.loot;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.DyeColor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables {

    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    @Override
    protected void addTables() {

        // Make Mod blocks drop themselves by default
        for (Block block : getKnownBlocks()) {
            dropSelf(block);
        }

        // Override dropSelf loot
        for (DyeColor color: DyeColor.values()) {
            add(ModBlocks.COLORED_LEAVES.get(color).get(), (block) -> createLeavesDrops(block, ModBlocks.COLORED_SAPLINGS.get(color).get(), NORMAL_LEAVES_SAPLING_CHANCES));
        }

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> ColorfulJourney.MOD_ID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }

}
