package net.mabbouxj.colorful_journey.utils;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;

public class MobUtils {

    public static void initFromOldEntity(MobEntity newEntity, MobEntity oldEntity) {
        initPosition(newEntity, oldEntity);
        initAttributes(newEntity, oldEntity);
        initEquipment(newEntity, oldEntity);
    }

    private static void initPosition(MobEntity newEntity, MobEntity oldEntity) {
        newEntity.setPos(
                (oldEntity.xOld + oldEntity.getX()) / 2.0D,
                (oldEntity.yOld + oldEntity.getY()) / 2.0D,
                (oldEntity.zOld + oldEntity.getZ()) / 2.0D
        );
        newEntity.setYHeadRot(oldEntity.getYHeadRot());
        newEntity.setSpeed(oldEntity.getSpeed());
        newEntity.setXxa(oldEntity.xxa);
        newEntity.setYya(oldEntity.yya);
        newEntity.setZza(oldEntity.zza);
    }

    private static void initAttributes(MobEntity newEntity, MobEntity oldEntity) {
        newEntity.setHealth(oldEntity.getHealth() * newEntity.getMaxHealth() / oldEntity.getMaxHealth());
        newEntity.setAggressive(oldEntity.isAggressive());
        newEntity.setTarget(oldEntity.getTarget());
        newEntity.setCanPickUpLoot(oldEntity.canPickUpLoot());
        newEntity.setArrowCount(oldEntity.getArrowCount());
        newEntity.setBaby(oldEntity.isBaby());
        if (newEntity instanceof AgeableEntity && oldEntity instanceof AgeableEntity) {
            ((AgeableEntity) newEntity).setAge(((AgeableEntity) oldEntity).getAge());
        }
    }

    private static void initEquipment(MobEntity newEntity, MobEntity oldEntity) {
        newEntity.setItemSlot(EquipmentSlotType.HEAD, oldEntity.getItemBySlot(EquipmentSlotType.HEAD));
        newEntity.setItemSlot(EquipmentSlotType.CHEST, oldEntity.getItemBySlot(EquipmentSlotType.CHEST));
        newEntity.setItemSlot(EquipmentSlotType.LEGS, oldEntity.getItemBySlot(EquipmentSlotType.LEGS));
        newEntity.setItemSlot(EquipmentSlotType.FEET, oldEntity.getItemBySlot(EquipmentSlotType.FEET));
        newEntity.setItemSlot(EquipmentSlotType.MAINHAND, oldEntity.getItemBySlot(EquipmentSlotType.MAINHAND));
        newEntity.setItemSlot(EquipmentSlotType.OFFHAND, oldEntity.getItemBySlot(EquipmentSlotType.OFFHAND));
        newEntity.setItemInHand(Hand.MAIN_HAND, oldEntity.getMainHandItem());
        newEntity.setItemInHand(Hand.OFF_HAND, oldEntity.getOffhandItem());
    }

}
