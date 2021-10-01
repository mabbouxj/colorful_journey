package net.mabbouxj.colorful_journey.tiles;


import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.annotations.Sync;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EaselTile extends BasicTile {

    public static final ResourceLocation EMPTY_SLATE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel_slate.png");
    public static final ResourceLocation PAINT_UGLY_SLATE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel_paint_ugly.png");
    public static final ResourceLocation PAINT0_SLATE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel_paint_0.png");
    public static final ResourceLocation PAINT1_SLATE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel_paint_1.png");
    @Sync
    public boolean hasSlate = false;
    @Sync
    public boolean hasPaint = false;
    @Sync
    public int currentPaint = -1;

    public EaselTile() {
        super(ModTiles.EASEL.get());
    }

    public boolean popSlate(World world, BlockPos pos) {
        if (!hasSlate || this.level == null)
            return false;
        ItemStack slate = new ItemStack(Items.PAPER, 1);
        InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), slate);
        hasSlate = false;
        hasPaint = false;
        return true;
    }

    public ResourceLocation getSlateTexture() {
        return EMPTY_SLATE_TEX;
    }

    public void changePaint() {
        if (!hasSlate) return;
        currentPaint = new Random().nextInt(10);
        hasPaint = true;
    }

    public ResourceLocation getPaintTexture() {
        switch (currentPaint) {
            case 0:
                return PAINT0_SLATE_TEX;
            case 1:
                return PAINT1_SLATE_TEX;
            default:
                return PAINT_UGLY_SLATE_TEX;
        }
    }

}
