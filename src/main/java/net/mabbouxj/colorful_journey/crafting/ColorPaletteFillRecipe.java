package net.mabbouxj.colorful_journey.crafting;

import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModRecipeSerializers;
import net.mabbouxj.colorful_journey.items.ColorPaletteItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

// Add a craft to repair the color gun with ink balls in crafting grid
public class ColorPaletteFillRecipe extends SpecialRecipe {

    private int dyeUsedToRepair = 0;

    public ColorPaletteFillRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public ItemStack assemble(CraftingInventory inventory) {

        ItemStack colorPaletteStack = ItemStack.EMPTY;
        NonNullList<ItemStack> dyeStacks = NonNullList.create();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);

            if (slotStack.isEmpty())
                continue;

            if (colorPaletteStack.isEmpty() && slotStack.isDamageableItem() && slotStack.getItem() instanceof ColorPaletteItem) {
                colorPaletteStack = slotStack;
            } else if (slotStack.getItem().is(Tags.Items.DYES)) {
                dyeStacks.add(slotStack);
            }
        }

        if (colorPaletteStack.isEmpty() || dyeStacks.isEmpty()) {
            return ItemStack.EMPTY;
        }


        int totalDyes = 0;
        boolean maxed = false;

        for (ItemStack stack : dyeStacks) {
            if (maxed) return ItemStack.EMPTY;

            int nbDyes = stack.getCount();
            if (nbDyes == 0) return ItemStack.EMPTY;

            totalDyes += nbDyes;

            if (colorPaletteStack.getDamageValue() - totalDyes <= 0) {
                maxed = true;
            }
        }

        ItemStack repairedColorPalette = new ItemStack(ModItems.COLOR_PALETTE.get(), 1);
        repairedColorPalette.setDamageValue(colorPaletteStack.getDamageValue() - totalDyes);

        // -1 because vanilla crafting use 1 of each material by default
        dyeUsedToRepair = colorPaletteStack.getDamageValue() - repairedColorPalette.getDamageValue() - 1;

        return repairedColorPalette;
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
        return ModRecipeSerializers.COLOR_PALETTE_FILL_RECIPE.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);

            if (slotStack.isEmpty())
                continue;
            if (dyeUsedToRepair == 0)
                continue;

            if (slotStack.getItem().is(Tags.Items.DYES)) {
                if (slotStack.getCount() <= dyeUsedToRepair) {
                    dyeUsedToRepair -= slotStack.getCount();
                    inventory.getItem(i).setCount(0);
                } else {
                    inventory.getItem(i).setCount(slotStack.getCount() - dyeUsedToRepair);
                    dyeUsedToRepair = 0;
                }
            }
        }
        return NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
    }

}
