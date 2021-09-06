package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.DyeColor;
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

    public Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<Block, Block>() {{
        for (DyeColor color: DyeColor.values()) {
            put(ModBlocks.COLORED_LOGS.get(color).get(), ModBlocks.COLORED_STRIPPED_LOGS.get(color).get());
            put(ModBlocks.COLORED_WOODS.get(color).get(), ModBlocks.COLORED_STRIPPED_WOODS.get(color).get());
        }
    }};

    @SubscribeEvent
    public void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof AxeItem) {
            World world = event.getWorld();
            BlockPos blockpos = event.getPos();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
            if (block != null) {
                PlayerEntity playerentity = event.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide()) {
                    world.setBlock(blockpos, block.defaultBlockState()
                            .setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)), 11);
                    if (playerentity != null) {
                        event.getItemStack().hurtAndBreak(1, playerentity, (entity) -> {
                            entity.broadcastBreakEvent(event.getHand());
                        });
                    }
                }
            }
        }

    }


}
