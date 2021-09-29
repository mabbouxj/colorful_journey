package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.capabilities.ItemEnergyStorageCapability;
import net.mabbouxj.colorful_journey.client.sounds.LaserLoopSound;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;


public class ColorLaserGunItem extends Item {

    public static final Integer LASER_RANGE = 100;
    public static final float LASER_THICKNESS = 0.04f;
    private LaserLoopSound laserLoopSound;
    private boolean isRandom = true;
    private Integer nbTicksChangeRandomColor = 20;
    private Integer randomColorTimer = 0;
    private int energyCapacity;

    public ColorLaserGunItem() {
        super(new Item.Properties()
                .tab(ColorfulJourney.MOD_ITEM_GROUP)
                .stacksTo(1)
                .setNoRepair());
        this.energyCapacity = ModConfigs.COMMON.LASER_GUN_BUFFER_CAPACITY.get();
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.energyCapacity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null)
                .map(e -> 1D - (e.getEnergyStored() / (double) e.getMaxEnergyStored()))
                .orElse(0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Objects.requireNonNull(TextFormatting.AQUA.getColor());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyStorageCapability(stack, ModConfigs.COMMON.LASER_GUN_BUFFER_CAPACITY.get());
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            if (new Random().nextInt(100) > 80) {
                isRandom = true;
                ColorUtils.setColor(stack, ColorUtils.getRandomEnabledColor());
                player.displayClientMessage(new TranslationTextComponent("message.colorful_journey.color_set_to", "random"), true);
                return ActionResult.pass(stack);
            }
            isRandom = false;
            DyeColor color = ColorUtils.getRandomEnabledColor();
            ColorUtils.setColor(stack, color);
            player.displayClientMessage(new TranslationTextComponent("message.colorful_journey.color_set_to", ColorUtils.coloredColorName(color)), true);
            return ActionResult.pass(stack);
        }

        player.playSound(ModSounds.LASER_START.get(), 0.5f, 1);

        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (energy.getEnergyStored() == 0 && !player.isCreative()) {
            player.stopUsingItem();
            return ActionResult.fail(stack);
        }

        player.startUsingItem(hand);
        return ActionResult.pass(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void playLoopSound(LivingEntity player, ItemStack stack) {
        float volume = 0.5f;
        PlayerEntity myplayer = Minecraft.getInstance().player;
        if (myplayer.equals(player)) {
            if (volume != 0.0f) {
                if (laserLoopSound == null) {
                    laserLoopSound = new LaserLoopSound((PlayerEntity) player, volume);
                    Minecraft.getInstance().getSoundManager().play(laserLoopSound);
                }
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (player.level.isClientSide) {
            this.playLoopSound(player, stack);
        }

        if(isRandom && randomColorTimer++ >= nbTicksChangeRandomColor) {
            ColorUtils.setColor(stack, ColorUtils.getRandomEnabledColor());
            randomColorTimer = 0;
        }

        if (!player.level.isClientSide && player instanceof PlayerEntity) {

            IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
            energy.extractEnergy(ModConfigs.COMMON.LASER_GUN_ENERGY_COST_PER_TICK.get(), false);
            if (energy.getEnergyStored() == 0 && !((PlayerEntity) player).isCreative()) {
                player.stopUsingItem();
                return;
            }

            Vector3d laserHitLocation = player.pick(LASER_RANGE, 0.0F, false).getLocation();
            Optional<Entity> entityOptional = LaserUtils.getFirstEntityOnLine(player.level, getLaserGunCannonPosition((PlayerEntity) player), laserHitLocation, e -> true);
            BlockState hitBlock = player.level.getBlockState(new BlockPos(laserHitLocation));

            ParticleUtils.makeParticles(player.level, ColorUtils.getColor(stack), 2, 1, getLaserGunCannonPosition((PlayerEntity) player));
            ParticleUtils.makeParticles(player.level, ColorUtils.getColor(stack), 4, 20, laserHitLocation);

            if (entityOptional.isPresent()) {
                Entity entity = entityOptional.get();
                Entity newEntity = MobUtils.getNewColoredEntityFrom(player, entity, ColorUtils.getColor(stack));
                if (newEntity != null) {
                    entity.remove();
                    player.level.addFreshEntity(newEntity);
                    energy.extractEnergy(ModConfigs.COMMON.LASER_GUN_ENERGY_COST_PER_TRANSFORMATION.get(), false);
                }
            } else {
                Map<DyeColor, RegistryObject<? extends Block>> targets = ColorfulJourney.REPLACEMENT_BLOCKS.getOrDefault(hitBlock.getBlock(), null);
                if (targets != null) {
                    try {
                        Block newBlock = targets.get(ColorUtils.getColor(stack)).get();
                        player.level.setBlock(new BlockPos(laserHitLocation), newBlock.defaultBlockState(), 3);
                        energy.extractEnergy(ModConfigs.COMMON.LASER_GUN_ENERGY_COST_PER_TRANSFORMATION.get(), false);
                    } catch (Exception e) {
                        ColorfulJourney.LOGGER.info("Could not create replacement block for " + hitBlock.getBlock().getName());
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, World world, LivingEntity player, int ticks) {
        if (world.isClientSide) {
            if (laserLoopSound != null) {
                float volume = 0.5f;
                if (volume != 0.0f && !laserLoopSound.isStopped()) {
                    player.playSound(ModSounds.LASER_END.get(), volume * 0.5f, 1f);
                }
                laserLoopSound = null;
            }
        }

        randomColorTimer = 0;
        if (isRandom) {
            ColorUtils.setColor(itemStack, ColorUtils.getRandomEnabledColor());
        }

        if (player instanceof PlayerEntity)
            player.stopUsingItem();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public UseAction getUseAnimation(ItemStack itemStack) {
        return UseAction.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    // Try to get the position of the gun cannon
    private Vector3d getLaserGunCannonPosition(PlayerEntity player) {
        Vector3d look = player.getLookAngle();
        Vector3d right = new Vector3d(-look.z, 0, look.x).normalize();
        Vector3d forward = look;
        Vector3d down = right.cross(forward);

        right = right.scale(0.45f);
        forward = forward.scale(3f);
        down = down.scale(-0.35);

        Vector3d laserPos = player.getEyePosition(1).add(right);
        laserPos = laserPos.add(forward);
        laserPos = laserPos.add(down);

        return laserPos;
    }

    public static ItemStack getGun(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof ColorLaserGunItem)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof ColorLaserGunItem)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        DyeColor color = ColorUtils.getColor(itemStack);
        IEnergyStorage energy = itemStack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        tooltips.add(new TranslationTextComponent("screen.colorful_journey.energy", StringUtils.numberWithSuffix(energy.getEnergyStored()), StringUtils.numberWithSuffix(ModConfigs.COMMON.LASER_GUN_BUFFER_CAPACITY.get())).withStyle(TextFormatting.GREEN));
        if (isRandom) {
            tooltips.add(new StringTextComponent("Laser color: random"));
        } else {
            tooltips.add(new StringTextComponent("Laser color: " + ColorUtils.coloredColorName(color)));
        }
        tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.color_laser_gun"));
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
