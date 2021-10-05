package net.mabbouxj.colorful_journey.tiles;

import net.mabbouxj.colorful_journey.ModConfigs;
import net.mabbouxj.colorful_journey.annotations.Sync;
import net.mabbouxj.colorful_journey.blocks.EnergyDyeGeneratorBlock;
import net.mabbouxj.colorful_journey.capabilities.TileEnergyStorageCapability;
import net.mabbouxj.colorful_journey.components.InventoryComponent;
import net.mabbouxj.colorful_journey.containers.EnergyDyeGeneratorContainer;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.recipes.EnergyDyeGeneratorRecipe;
import net.mabbouxj.colorful_journey.utils.EnergyUtils;
import net.mabbouxj.colorful_journey.utils.RecipeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;


public class EnergyDyeGeneratorTile extends BasicTile implements ITickableTileEntity, INamedContainerProvider {

    public enum Slots {
        FUEL(0, (stack) -> ForgeHooks.getBurnTime(stack, null) > 0),
        INGREDIENT(1, (stack) -> true);

        int id;
        Predicate<ItemStack> filter;

        Slots(int number, Predicate<ItemStack> filter) {
            this.id = number;
            this.filter = filter;
        }

        public int getId() {
            return id;
        }
        public Predicate<ItemStack> getFilter() {
            return filter;
        }
    }

    @Sync
    public TileEnergyStorageCapability energyStorage;
    @Sync
    public InventoryComponent inventoryStorage;
    private final LazyOptional<TileEnergyStorageCapability> energy;
    private final LazyOptional<ItemStackHandler> inventory;

    public EnergyDyeGeneratorRecipe currentRecipe;
    @Sync
    public int remainingIngredient = 0;
    @Sync
    public int maxIngredient = 0;
    @Sync
    public int remainingFuel = 0;
    @Sync
    public int maxFuel = 0;
    @Sync
    public int generating = 0;

    public EnergyDyeGeneratorTile() {
        super(ModTiles.ENERGY_DYE_GENERATOR.get());
        this.energyStorage = new TileEnergyStorageCapability(this, ModConfigs.COMMON.ENERGY_DYE_GENERATOR_BUFFER_CAPACITY.get(), ModConfigs.COMMON.ENERGY_DYE_GENERATOR_MAX_IN_OUT.get(), true, false);
        this.energy = LazyOptional.of(() -> this.energyStorage);
        this.inventoryStorage = new InventoryComponent(this, Slots.values().length);
        this.inventory = LazyOptional.of(() -> inventoryStorage);
    }

    @Nullable
    @Override
    public Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
        assert level != null;
        return new EnergyDyeGeneratorContainer(this, i, playerInventory, this.inventory.orElse(new ItemStackHandler(Slots.values().length)));
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide)
            return;

        inventory.ifPresent(handler -> energy.ifPresent(buffer -> {

            EnergyUtils.extractToAdjacentBlocks(this.level, buffer, getBlockPos());

            if (currentRecipe == null) {
                setChanged();
                return;
            }

            if (remainingFuel <= 0)
                ignite(handler);
            consumeFuel();
            if (remainingFuel <= 0) {
                setChanged();
                return;
            }

            boolean canInsertEnergy = buffer.receiveEnergy(currentRecipe.energyPerTick, true) > 0;
            if (!canInsertEnergy)
                return;
            if (remainingIngredient <= 0)
                initBurn(handler);
            if (remainingIngredient > 0)
                generateEnergy(buffer);
            setChanged();
        }));

        if (isLit())
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyDyeGeneratorBlock.LIT, true), 3);
        else
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(EnergyDyeGeneratorBlock.LIT, false), 3);

    }

    private boolean isLit() {
        return remainingFuel > 0 && currentRecipe != null && remainingIngredient > 0;
    }

    private void generateEnergy(IEnergyStorage energyStorage) {
        int received = energyStorage.receiveEnergy(currentRecipe.energyPerTick, false);
        generating = received;
        remainingIngredient -= received;
        if (remainingIngredient <= 0) {
            currentRecipe = null;
            generating = 0;
        }
    }

    private void initBurn(ItemStackHandler handler) {
        if (remainingFuel > 0 && currentRecipe != null) {
            remainingIngredient = currentRecipe.energyTotal;
            maxIngredient = currentRecipe.energyTotal;
            handler.extractItem(Slots.INGREDIENT.id, 1, false);
        }
    }

    private void consumeFuel() {
        if (remainingFuel > 0)
            remainingFuel--;
        if (remainingFuel <= 0)
            maxFuel = 0;
    }

    private void ignite(ItemStackHandler handler) {
        ItemStack fuelStack = handler.getStackInSlot(Slots.FUEL.id);
        int burnTime = ForgeHooks.getBurnTime(fuelStack, null);
        if (burnTime > 0) {
            Item fuel = handler.getStackInSlot(Slots.FUEL.id).getItem();
            handler.extractItem(Slots.FUEL.id, 1, false);
            if(fuel instanceof BucketItem && fuel != Items.BUCKET)
                handler.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
            remainingFuel = (int) (Math.floor(burnTime) / ModConfigs.COMMON.ENERGY_DYE_GENERATOR_FUEL_CONSUMPTION_SPEED.get());
            maxFuel = remainingFuel;
        }
    }

    private void checkForRecipe() {
        if (this.level == null || this.level.isClientSide || currentRecipe != null)
            return;
        inventory.ifPresent(inventory -> energy.ifPresent(buffer -> {
            if (!inventory.getStackInSlot(Slots.FUEL.getId()).isEmpty() || remainingFuel > 0) {
                currentRecipe = RecipeUtils.getRecipes(this.level, ModRecipeTypes.ENERGY_DYE_GENERATOR).stream()
                        .filter(recipe -> recipe.matches(inventory, buffer)).findFirst().orElse(null);
            }
            if (currentRecipe == null) {
                remainingIngredient = 0;
                maxIngredient = 0;
            }
        }));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        if (cap == CapabilityEnergy.ENERGY)
            return energy.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }

    @Override
    public void setRemoved() {
        energy.invalidate();
        inventory.invalidate();
        super.setRemoved();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.energy_dye_generator");
    }

}
