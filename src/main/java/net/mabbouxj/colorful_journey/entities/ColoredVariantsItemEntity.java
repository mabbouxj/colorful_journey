package net.mabbouxj.colorful_journey.entities;

import net.mabbouxj.colorful_journey.items.ColoredEnderPearlItem;
import net.mabbouxj.colorful_journey.items.ColoredVariantsItem;
import net.mabbouxj.colorful_journey.utils.ParticleUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ColoredVariantsItemEntity extends ItemEntity {

    public ColoredVariantsItemEntity(World worldIn, Entity location, ItemStack stack) {
        super(worldIn, location.getX(), location.getY(), location.getZ(), stack);
        this.setDeltaMovement(location.getDeltaMovement());
        this.setPickUpDelay(40);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()) {
            BlockPos pos = this.getOnPos();
            Item newItem = null;
            DyeColor color = DyeColor.WHITE;
            this.kill();

            if (this.getItem().getItem() instanceof ColoredVariantsItem) {
                newItem = ((ColoredVariantsItem) this.getItem().getItem()).getInitialItem();
                color = ((ColoredVariantsItem) this.getItem().getItem()).getColor();
            } else if (this.getItem().getItem() instanceof ColoredEnderPearlItem) {
                newItem = Items.ENDER_PEARL;
                color = ((ColoredEnderPearlItem) this.getItem().getItem()).getColor();
            }

            if (newItem != null) {
                InventoryHelper.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(newItem, this.getItem().getCount()));
                ParticleUtils.makeParticles(this.level, color, 4, 20, new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
            }
        }
    }
}
