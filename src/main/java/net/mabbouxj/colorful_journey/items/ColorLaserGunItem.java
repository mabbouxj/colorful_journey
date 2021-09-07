package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.client.particles.InkSplashParticle;
import net.mabbouxj.colorful_journey.client.sounds.LaserLoopSound;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.LaserUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class ColorLaserGunItem extends Item {

    public static final Integer LASER_RANGE = 100;
    public static final float LASER_THICKNESS = 0.04f;
    private LaserLoopSound laserLoopSound;
    private boolean isRandom = true;
    private Integer nbTicksChangeRandomColor = 20;
    private Integer randomColorTimer = 0;

    public ColorLaserGunItem() {
        super(new Item.Properties()
                .tab(ColorfulJourney.MOD_ITEM_GROUP)
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            if (new Random().nextInt(100) > 80) {
                isRandom = true;
                ColorUtils.setColor(stack, ColorUtils.getRandomEnableColor());
                player.displayClientMessage(new StringTextComponent("Color set to: random"), true);
                return ActionResult.pass(stack);
            }
            isRandom = false;
            DyeColor color = ColorUtils.getRandomEnableColor();
            ColorUtils.setColor(stack, color);
            player.displayClientMessage(new StringTextComponent("Color set to: " + ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + color.getName()), true);
            return ActionResult.pass(stack);
        }

        player.playSound(ModSounds.LASER_START.get(), 1, 1);

        player.startUsingItem(hand);
        return ActionResult.pass(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void playLoopSound(LivingEntity player, ItemStack stack) {
        float volume = 1;
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
            ColorUtils.setColor(stack, ColorUtils.getRandomEnableColor());
            randomColorTimer = 0;
        }

        if (!player.level.isClientSide && player instanceof PlayerEntity) {

            Vector3d laserHitLocation = player.pick(LASER_RANGE, 0.0F, false).getLocation();
            Optional<Entity> entityOptional = LaserUtils.getFirstEntityOnLine(player.level, getLaserGunCannonPosition((PlayerEntity) player), laserHitLocation, e -> true);

            makeParticle(player.level, (PlayerEntity) player, 2, 1, ColorUtils.getColor(stack));
            makeParticle(player.level, 3, 20, ColorUtils.getColor(stack), laserHitLocation);

            if (entityOptional.isPresent()) {
                Entity entity = entityOptional.get();
                Entity newEntity = MobUtils.getNewColoredEntityFrom(player, entity, ColorUtils.getColor(stack));
                if (newEntity != null) {
                    entity.remove();
                    player.level.addFreshEntity(newEntity);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, World world, LivingEntity player, int ticks) {
        if (world.isClientSide) {
            if (laserLoopSound != null) {
                float volume = 1;
                if (volume != 0.0f && !laserLoopSound.isStopped()) {
                    player.playSound(ModSounds.LASER_END.get(), volume * 0.5f, 1f);
                }
                laserLoopSound = null;
            }
        }

        randomColorTimer = 0;
        ColorUtils.setColor(itemStack, ColorUtils.getRandomEnableColor());

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

    private void makeParticle(World world, PlayerEntity player, double speed, int amount, DyeColor color) {
        Vector3d laserPos = getLaserGunCannonPosition(player);
        makeParticle(world, speed, amount, color, laserPos);
    }

    private void makeParticle(World world, double speed, int amount, DyeColor color, Vector3d position) {
        final double TICKS_PER_SECOND = 20;
        final double SPEED_IN_BLOCKS_PER_TICK = speed / TICKS_PER_SECOND;
        for (int i = 0; i < amount; i++) {
            Vector3d direction = new Vector3d(
                    2 * new Random().nextDouble() - 1,
                    2 * new Random().nextDouble() - 1,
                    2 * new Random().nextDouble() - 1
            );
            double velocityX = SPEED_IN_BLOCKS_PER_TICK * direction.x;
            double velocityY = SPEED_IN_BLOCKS_PER_TICK * direction.y;
            double velocityZ = SPEED_IN_BLOCKS_PER_TICK * direction.z;

            world.addParticle(
                    new InkSplashParticle.Data(color),
                    position.x, position.y, position.z,
                    velocityX, velocityY, velocityZ
            );
        }
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
        if (isRandom) {
            tooltips.add(new StringTextComponent("Laser color: random"));
        } else {
            tooltips.add(new StringTextComponent("Laser color: " + ColorUtils.DYE_COLOR_TO_TEXT_FORMAT.get(color.getId()) + color.getName()));
        }
        tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.color_laser_gun"));
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
