package net.mabbouxj.colorful_journey.recipes;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModRecipeSerializers;
import net.mabbouxj.colorful_journey.items.ColorGunItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

// Add a craft to repair the color gun with ink balls in crafting grid
public class ColorGunFillRecipe extends SpecialRecipe {

    private int inkBallUsedToRepair = 0;

    public ColorGunFillRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public ItemStack assemble(CraftingInventory inventory) {

        ItemStack colorGunStack = ItemStack.EMPTY;
        NonNullList<ItemStack> inkBallStacks = NonNullList.create();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);

            if (slotStack.isEmpty())
                continue;

            if (colorGunStack.isEmpty() && slotStack.isDamageableItem() && slotStack.getItem() instanceof ColorGunItem) {
                colorGunStack = slotStack;
            } else if (slotStack.getItem().equals(ModItems.INK_BALL.get())) {
                inkBallStacks.add(slotStack);
            }
        }

        if (colorGunStack.isEmpty() || inkBallStacks.isEmpty()) {
            return ItemStack.EMPTY;
        }


        int totalInkBall = 0;
        boolean maxed = false;

        for (ItemStack inkBallStack : inkBallStacks) {
            if (maxed) return ItemStack.EMPTY;

            int nbInkBall = inkBallStack.getCount();
            if (nbInkBall == 0) return ItemStack.EMPTY;

            totalInkBall += nbInkBall;

            if (colorGunStack.getDamageValue() - totalInkBall <= 0) {
                maxed = true;
            }
        }

        ItemStack repairedColorGun = new ItemStack(ModItems.COLOR_GUN.get(), 1);
        repairedColorGun.setDamageValue(colorGunStack.getDamageValue() - totalInkBall);

        // -1 because vanilla crafting use 1 of each material by default
        inkBallUsedToRepair = colorGunStack.getDamageValue() - repairedColorGun.getDamageValue() - 1;

        return repairedColorGun;
    }


    @Override
    public boolean matches(CraftingInventory inventory, World worldIn) {
        return !this.assemble(inventory).isEmpty();
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COLOR_GUN_FILL.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);

            if (slotStack.isEmpty())
                continue;
            if (inkBallUsedToRepair == 0)
                continue;

            if (slotStack.getItem().equals(ModItems.INK_BALL.get())) {
                if (slotStack.getCount() <= inkBallUsedToRepair) {
                    inkBallUsedToRepair -= slotStack.getCount();
                    inventory.getItem(i).setCount(0);
                } else {
                    inventory.getItem(i).setCount(slotStack.getCount() - inkBallUsedToRepair);
                    inkBallUsedToRepair = 0;
                }
            }
        }
        return NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
    }

}
