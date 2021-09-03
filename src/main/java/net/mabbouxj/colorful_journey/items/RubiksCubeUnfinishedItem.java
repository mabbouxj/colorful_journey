package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RubiksCubeUnfinishedItem extends BlockItem {

    private static final Integer MIN_USE_TO_SOLVE = 10;
    private static final Integer MAX_USE_TO_SOLVE = 50;
    private static final Integer NB_MIX_VARIANTS = 3;

    public RubiksCubeUnfinishedItem() {
        super(ModBlocks.RUBIKS_CUBE_UNFINISHED.get(), new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundNBT nbt = stack.getOrCreateTag();
        Integer usageCount = getUsageCount(stack);
        Integer timeToSolve = getTimeToSolve(stack);
        usageCount++;
        nbt.putInt("usageCount", usageCount);
        nbt.putInt("timeToSolve", timeToSolve);
        if (usageCount >= timeToSolve) {
            player.setItemInHand(hand, new ItemStack(ModItems.RUBIKS_CUBE.get(), 1));
        }
        return ActionResult.success(stack);
    }

    private static Integer getTimeToSolve(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("timeToSolve") ? nbt.getInt("timeToSolve"): new Random().nextInt(MAX_USE_TO_SOLVE - MIN_USE_TO_SOLVE) + MIN_USE_TO_SOLVE;
    }

    private static Integer getUsageCount(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("usageCount") ? nbt.getInt("usageCount"): 0;
    }

    public static Integer getMixVariant(ItemStack stack) {
        return getUsageCount(stack) % NB_MIX_VARIANTS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        TranslationTextComponent tooltip = new TranslationTextComponent("tooltip.colorful_journey.rubiks_cube_unfinished");
        if (!tooltip.getString().contains("tooltip.colorful_journey")) {
            if (Screen.hasShiftDown()) {
                tooltips.add(tooltip);
            } else {
                tooltips.add(new TranslationTextComponent("tooltip.colorful_journey.hold_shift_for_info"));
            }
        }
        super.appendHoverText(itemStack, world, tooltips, flag);
    }

}
