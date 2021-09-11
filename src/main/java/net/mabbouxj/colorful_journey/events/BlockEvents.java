package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.blocks.ColoredGrassBlock;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class BlockEvents {

    private final Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<Block, Block>() {{
        for (DyeColor color: DyeColor.values()) {
            put(ModBlocks.COLORED_LOGS.get(color).get(), ModBlocks.COLORED_STRIPPED_LOGS.get(color).get());
            put(ModBlocks.COLORED_WOODS.get(color).get(), ModBlocks.COLORED_STRIPPED_WOODS.get(color).get());
        }
    }};
    private final Map<Block, Block> GRASS_TO_PATH_MAP = new HashMap<Block, Block>() {{
        for (DyeColor color: DyeColor.values()) {
            put(ModBlocks.COLORED_GRASS_BLOCKS.get(color).get(), ModBlocks.COLORED_GRASS_PATH.get(color).get());
        }
    }};

    @SubscribeEvent
    public void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) {

        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        BlockPos blockpos = event.getPos();
        BlockState blockstate = world.getBlockState(blockpos);

        if (event.getItemStack().getItem() instanceof AxeItem) {
            Block block = BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
            if (block != null) {
                world.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide()) {
                    world.setBlock(blockpos, block.defaultBlockState()
                            .setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)), 11);
                    if (player != null) {
                        event.getItemStack().hurtAndBreak(1, player, (entity) -> {
                            entity.broadcastBreakEvent(event.getHand());
                        });
                    }
                }
            }
        } else if (event.getItemStack().getItem() instanceof ShovelItem) {
            Block block = GRASS_TO_PATH_MAP.get(blockstate.getBlock());
            if (block != null) {
                world.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide()) {
                    world.setBlock(blockpos, block.defaultBlockState(), 11);
                    if (player != null) {
                        event.getItemStack().hurtAndBreak(1, player, (entity) -> {
                            entity.broadcastBreakEvent(event.getHand());
                        });
                    }
                }
            }
        } else if (event.getItemStack().getItem() instanceof HoeItem) {
            if (blockstate.getBlock() instanceof ColoredGrassBlock) {
                world.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide()) {
                    world.setBlock(blockpos, Blocks.FARMLAND.defaultBlockState(), 11);
                    if (player != null) {
                        event.getItemStack().hurtAndBreak(1, player, (entity) -> {
                            entity.broadcastBreakEvent(event.getHand());
                        });
                    }
                }
            }
        }

    }


}
