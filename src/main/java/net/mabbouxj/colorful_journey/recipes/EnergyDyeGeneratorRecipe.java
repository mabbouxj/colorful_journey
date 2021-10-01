package net.mabbouxj.colorful_journey.recipes;

import com.google.gson.JsonObject;
import net.mabbouxj.colorful_journey.init.ModRecipeSerializers;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.tiles.EnergyDyeGeneratorTile;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EnergyDyeGeneratorRecipe implements IRecipe<IInventory> {

    protected ResourceLocation id;
    public Ingredient ingredient;
    public int energyPerTick;
    public int energyTotal;

    public EnergyDyeGeneratorRecipe(ResourceLocation id) {
        this(id, Ingredient.EMPTY, 0, 0);
    }

    public EnergyDyeGeneratorRecipe(ResourceLocation id, Ingredient ingredient, int energyPerTick, int energyTotal) {
        this.id = id;
        this.ingredient = ingredient;
        this.energyPerTick = energyPerTick;
        this.energyTotal = energyTotal;
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        return false;
    }

    public boolean matches(IItemHandler inventory, IEnergyStorage energy) {
        if (ingredient == null || ingredient.isEmpty()) return false;

        boolean ingredientOk = ingredient.test(inventory.getStackInSlot(EnergyDyeGeneratorTile.Slots.INGREDIENT.getId()));
        boolean energyOk = false;
        if (energy.receiveEnergy(energyPerTick, true) > 0) {
            energyOk = true;
        }

        return ingredientOk && energyOk;
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ENERGY_DYE_GENERATOR.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.ENERGY_DYE_GENERATOR;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<EnergyDyeGeneratorRecipe> {

        @Override
        public EnergyDyeGeneratorRecipe fromJson(ResourceLocation location, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            int energyPerTick = json.get("energy_per_tick").getAsInt();
            int energyTotal = json.get("energy_total").getAsInt();

            return new EnergyDyeGeneratorRecipe(location, ingredient, energyPerTick, energyTotal);
        }

        @Nullable
        @Override
        public EnergyDyeGeneratorRecipe fromNetwork(ResourceLocation location, PacketBuffer packetBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(packetBuffer);
            int energyPerTick = packetBuffer.readInt();
            int energyTotal = packetBuffer.readInt();
            return new EnergyDyeGeneratorRecipe(location, ingredient, energyPerTick, energyTotal);
        }

        @Override
        public void toNetwork(PacketBuffer packetBuffer, EnergyDyeGeneratorRecipe recipe) {
            recipe.ingredient.toNetwork(packetBuffer);
            packetBuffer.writeInt(recipe.energyPerTick);
            packetBuffer.writeInt(recipe.energyTotal);
        }
    }

    public static class Builder {

        private final EnergyDyeGeneratorRecipe recipe;

        public Builder(ResourceLocation id) {
            recipe = new EnergyDyeGeneratorRecipe(id);
        }

        public Builder withIngredient(IItemProvider ingredient) {
            recipe.ingredient = Ingredient.of(ingredient);
            return this;
        }

        public Builder withIngredient(ITag<Item> ingredient) {
            recipe.ingredient = Ingredient.of(ingredient);
            return this;
        }

        public Builder withEnergyGeneration(int perTick, int total) {
            recipe.energyPerTick = perTick;
            recipe.energyTotal = total;
            return this;
        }

        public void save(Consumer<IFinishedRecipe> consumer) {
            consumer.accept(new EnergyDyeGeneratorRecipe.Result(this.recipe));
        }

    }

    public static class Result implements IFinishedRecipe {

        private final EnergyDyeGeneratorRecipe recipe;

        public Result(EnergyDyeGeneratorRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", recipe.ingredient.toJson());
            json.addProperty("energy_per_tick", recipe.energyPerTick);
            json.addProperty("energy_total", recipe.energyTotal);
        }

        @Override
        public ResourceLocation getId() {
            return recipe.getId();
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return ModRecipeSerializers.ENERGY_DYE_GENERATOR.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

}
