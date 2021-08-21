package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.Reference;
import net.mabbouxj.colorful_journey.entities.InkBallEntity;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Objects;


public class ColorGunItem extends Item {

    private final static int MAX_DAMAGE = 256;

    public ColorGunItem() {
        super(new Item.Properties()
                .tab(Reference.MOD_ITEM_GROUP)
                .stacksTo(1)
                .durability(MAX_DAMAGE)
        );
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack colorGun = player.getItemInHand(hand);

        if (colorGun.getDamageValue() == colorGun.getMaxDamage()) {
            TranslationTextComponent message = new TranslationTextComponent("message.colorful_journey.color_gun_out_of_ink");
            player.displayClientMessage(message, true);
            return ActionResult.pass(colorGun);
        }

        world.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.COLOR_GUN_SHOOT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (!world.isClientSide()) {
            InkBallEntity inkBallEntity = new InkBallEntity(world, player);
            inkBallEntity.setItem(new ItemStack(ModItems.INK_BALL.get()));

            float velocity = 2.0F;
            inkBallEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, velocity, 1.0F);
            world.addFreshEntity(inkBallEntity);

        }

        if (!player.isCreative()) {
            colorGun.setDamageValue(colorGun.getDamageValue() + 1);
        }

        return ActionResult.success(colorGun);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Objects.requireNonNull(TextFormatting.AQUA.getColor());
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

}