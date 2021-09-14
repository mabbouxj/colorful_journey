package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.mabbouxj.colorful_journey.utils.MobUtils;
import net.mabbouxj.colorful_journey.utils.ParticleUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PaintbrushItem extends Item {

    public PaintbrushItem() {
        super(new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(1).durability(64));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack paintbrush = player.getMainHandItem();
        ItemStack colorPalette = player.getOffhandItem();

        if (!(paintbrush.getItem() instanceof PaintbrushItem) || !(colorPalette.getItem() instanceof ColorPaletteItem)) {
            TranslationTextComponent message = new TranslationTextComponent("message.colorful_journey.paintbrush_usage");
            player.displayClientMessage(message, true);
            return ActionResult.fail(paintbrush);
        }

        if (!world.isClientSide) {
            if (colorPalette.getDamageValue() == colorPalette.getMaxDamage()) {
                TranslationTextComponent message = new TranslationTextComponent("message.colorful_journey.color_palette_out_of_ink");
                player.displayClientMessage(message, true);
                return ActionResult.pass(paintbrush);
            }

            if (!player.isCreative()) {
                colorPalette.setDamageValue(colorPalette.getDamageValue() + 1);
            }

            DyeColor color = ColorUtils.getRandomEnabledColor();
            ColorUtils.setColor(paintbrush, color);
            ParticleUtils.makeParticles(world, color, 8, 20, new Vector3d(player.getRandomX(0.5), player.getRandomY(), player.getRandomZ(0.5)));
            player.displayClientMessage(new TranslationTextComponent("message.colorful_journey.color_set_to", ColorUtils.coloredColorName(color)), true);

        }

        return ActionResult.success(paintbrush);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity player) {
        if (!ColorUtils.hasColor(stack)) {
            return false;
        }

        Entity newEntity = MobUtils.getNewColoredEntityFrom(player, enemy, ColorUtils.getColor(stack));
        if (newEntity != null) {
            enemy.remove();
            player.level.addFreshEntity(newEntity);
            ParticleUtils.makeParticles(player.level, ColorUtils.getColor(stack), 8, 20, new Vector3d(newEntity.getRandomX(0.5), newEntity.getRandomY(), newEntity.getRandomZ(0.5)));
        }

        stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        ColorUtils.removeColor(stack);
        return false;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Objects.requireNonNull(TextFormatting.GREEN.getColor());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        if (ColorUtils.hasColor(stack)) {
            DyeColor color = ColorUtils.getColor(stack);
            tooltips.add(new StringTextComponent("Color: " + ColorUtils.coloredColorName(color)));
        }
        tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.paintbrush"));
    }
}
