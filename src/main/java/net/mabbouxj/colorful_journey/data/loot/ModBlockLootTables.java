package net.mabbouxj.colorful_journey.data.loot;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        // Make Mod blocks drop themselves
        for (Block block : getKnownBlocks()) {
            dropSelf(block);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> ColorfulJourney.MOD_ID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }

}
