package net.mabbouxj.colorful_journey.events;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.entities.IColoredMobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class MobEvents {

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

            Map<DyeColor, RegistryObject<? extends Item>> replacementItems = ColorfulJourney.REPLACEMENT_ITEMS.getOrDefault(itemEntity.getItem().getItem(), null);

            if (replacementItems != null) {
                ItemStack newItemStack = new ItemStack(replacementItems.get(entityColor).get(), itemEntity.getItem().getCount());
                dropsToAdd.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newItemStack));
                dropsToRemove.add(itemEntity);
            }
        }

        e.getDrops().removeAll(dropsToRemove);
        e.getDrops().addAll(dropsToAdd);
    }

}
