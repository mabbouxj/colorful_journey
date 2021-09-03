package net.mabbouxj.colorful_journey.items;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RubiksCubeItem extends BlockItem {

    public RubiksCubeItem() {
        super(ModBlocks.RUBIKS_CUBE.get(), new Item.Properties().tab(ColorfulJourney.MOD_ITEM_GROUP).stacksTo(64));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            ItemStack unfinished = new ItemStack(ModItems.RUBIKS_CUBE_UNFINISHED.get(), 1);
            if (!player.inventory.add(unfinished)) {
                world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), unfinished));
            }
            stack.shrink(1);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        TranslationTextComponent tooltip = new TranslationTextComponent("tooltip.colorful_journey.rubiks_cube");
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
