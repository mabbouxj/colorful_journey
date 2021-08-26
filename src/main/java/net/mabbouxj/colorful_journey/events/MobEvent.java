package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.entities.IColoredMobEntity;
import net.mabbouxj.colorful_journey.items.ColoredVariantsBlockItem;
import net.mabbouxj.colorful_journey.items.ColoredVariantsItem;
import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MobEvent {

    @SubscribeEvent
    public void onMobItemDrop(LivingDropsEvent e) {

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

        IColoredMobEntity coloredEntity = ((IColoredMobEntity) entity);
        DyeColor entityColor = coloredEntity.getColor();

        // Iterate over items dropped by an IColoredMobEntity
        // Replace "vanilla" items by their "colorful" equivalent
        for (ItemEntity itemEntity : e.getDrops()) {

            Item replacementItem = coloredEntity.getReplacementItemFor(itemEntity.getItem().getItem(), entityColor);

            if (replacementItem instanceof ColoredVariantsItem || replacementItem instanceof ColoredVariantsBlockItem) {
                ItemStack newItemStack = new ItemStack(replacementItem, itemEntity.getItem().getCount());
                ColorUtils.setColor(newItemStack, entityColor);
                dropsToAdd.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newItemStack));
                dropsToRemove.add(itemEntity);
            }
        }

        e.getDrops().removeAll(dropsToRemove);
        e.getDrops().addAll(dropsToAdd);
    }

}
