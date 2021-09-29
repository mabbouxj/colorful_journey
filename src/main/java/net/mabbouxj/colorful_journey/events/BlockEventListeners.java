package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.blocks.ColoredGrassBlock;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.items.PaintbrushItem;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.ParticleUtils;
import net.mabbouxj.colorful_journey.world.ColorfulPortalSize;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber
public class BlockEventListeners {

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
            // Wood Strip
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
            // Grass path
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
            // Farmland
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

    @SubscribeEvent
    public void onBlockLeftClicked(PlayerInteractEvent.LeftClickBlock event) {

        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        BlockPos blockPos = event.getPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (stack.getItem() instanceof PaintbrushItem) {

            // Replace Block by its colorful variant
            Map<DyeColor, RegistryObject<? extends Block>> targets = ColorfulJourney.REPLACEMENT_BLOCKS.getOrDefault(blockState.getBlock(), null);
            if (targets != null) {
                try {
                    Block newBlock = targets.get(ColorUtils.getColor(stack)).get();
                    world.setBlock(blockPos, newBlock.defaultBlockState(), 3);
                    ParticleUtils.makeParticles(player.level, ColorUtils.getColor(stack), 8, 20, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
                    ColorUtils.removeColor(stack);
                } catch (Exception e) {
                    ColorfulJourney.LOGGER.info("Could not create replacement block for " + blockState.getBlock().getName());
                }
            }

            // Try to open Colorful Portal
            if (world.dimension() == World.OVERWORLD || world.dimension() == World.NETHER) {
                Optional<ColorfulPortalSize> optional = ColorfulPortalSize.findEmptyPortalShape(world, blockPos, Direction.Axis.X);
                optional =  ColorfulPortalSize.onTrySpawnPortal(world, blockPos, optional);
                optional.ifPresent(ColorfulPortalSize::createPortalBlocks);
            }

        }

    }

    @Cancelable
    public static class ColorfulPortalSpawnEvent extends BlockEvent {
        private final ColorfulPortalSize size;

        public ColorfulPortalSpawnEvent(IWorld world, BlockPos pos, BlockState state, ColorfulPortalSize size) {
            super(world, pos, state);
            this.size = size;
        }

        public ColorfulPortalSize getPortalSize()
        {
            return size;
        }
    }



}
