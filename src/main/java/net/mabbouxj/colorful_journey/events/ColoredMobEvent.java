package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.entities.IColoredMobEntity;
import net.mabbouxj.colorful_journey.items.ColorfulItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ColoredMobEvent {

    @SubscribeEvent
    public void coloredMobItemDrop(LivingDropsEvent e) {

        Entity entity = e.getEntity();
        World world = entity.getCommandSenderWorld();
        List<ItemEntity> dropsToAdd = new ArrayList<>();
        List<ItemEntity> dropsToRemove = new ArrayList<>();
        BlockPos pos = entity.blockPosition();

        if (world.isClientSide()) {
            return;
        }

        if (!(entity instanceof IColoredMobEntity)) {
            return;
        }

        // Iterate over items dropped by an IColoredMobEntity
        // Replace "vanilla" items by their "colorful" equivalent
        for (ItemEntity itemEntity : e.getDrops()) {
            ColorfulItem replacementItem = ((IColoredMobEntity) entity).getReplacementItemFor(itemEntity.getItem().getItem());
            if (replacementItem != null) {
                ItemStack newItemStack = new ItemStack(replacementItem, itemEntity.getItem().getCount());
                ColorfulItem.setColor(newItemStack, ((IColoredMobEntity) entity).getColor());
                dropsToAdd.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newItemStack));
                dropsToRemove.add(itemEntity);
            }
        }

        e.getDrops().removeAll(dropsToRemove);
        e.getDrops().addAll(dropsToAdd);
    }

}
