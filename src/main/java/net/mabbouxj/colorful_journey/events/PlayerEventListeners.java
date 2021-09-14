package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEventListeners {

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        World world = event.getWorld();
        Entity target = event.getTarget();
        ItemStack tool = event.getPlayer().getItemInHand(event.getHand());

        if (world == null || !tool.getItem().is(Tags.Items.SHEARS)) {
            return;
        }

        if (target instanceof HorseEntity || target instanceof DonkeyEntity) {
            BlockPos pos = event.getPos();
            InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.HORSEHAIR.get(), 1));
            tool.hurtAndBreak(1, event.getPlayer(), (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }

    }

}
