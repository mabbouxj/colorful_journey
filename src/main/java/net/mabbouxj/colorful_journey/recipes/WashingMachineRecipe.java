package net.mabbouxj.colorful_journey.recipes;

import com.google.gson.JsonObject;
import net.mabbouxj.colorful_journey.init.ModRecipeSerializers;
import net.mabbouxj.colorful_journey.init.ModRecipeTypes;
import net.mabbouxj.colorful_journey.tiles.WashingMachineTile;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class WashingMachineRecipe implements IRecipe<IInventory> {

    public static int DEFAULT_PROCESSING_TIME = 40;
    public static int DEFAULT_ENERGY_PER_TICK = 100;
    public static FluidStack DEFAULT_INPUT_FLUID = new FluidStack(Fluids.WATER, 250);

    protected ResourceLocation id;
    protected ItemStack inputStack;
    protected FluidStack inputFluid;
    protected int processingTime;
    protected int energyPerTick;
    protected ItemStack output;
    protected ItemStack outputAlt;

    public WashingMachineRecipe(ResourceLocation id) {
        this(id, ItemStack.EMPTY, DEFAULT_INPUT_FLUID, DEFAULT_PROCESSING_TIME, DEFAULT_ENERGY_PER_TICK, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public WashingMachineRecipe(ResourceLocation id, ItemStack inputStack, FluidStack inputFluid, int processingTime, int energyPerTick, ItemStack output, ItemStack outputAlt) {
        this.id = id;
        this.inputStack = inputStack;
        this.inputFluid = inputFluid;
        this.processingTime = processingTime;
        this.energyPerTick = energyPerTick;
        this.output = output;
        this.outputAlt = outputAlt;
    }

    @Override
    public boolean matches(@Nonnull IInventory inventory, @Nonnull World world) {
        return false;
    }

    public boolean matches(IItemHandler inventory, IFluidHandler tank, IEnergyStorage energy) {
        if (inputStack == null || tank == null || inputFluid == null) return false;

        ItemStack ingredientStack = inventory.getStackInSlot(WashingMachineTile.Slots.INPUT.getId());
        boolean itemOk = false;
        boolean fluidOk = false;
        boolean energyOk = false;
        if (ingredientStack.getItem().equals(inputStack.getItem()) && ingredientStack.getCount() >= inputStack.getCount()) {
            itemOk = true;
        }
        if (tank.drain(inputFluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == inputFluid.getAmount()) {
            fluidOk = true;
        }
        if (energy.extractEnergy(energyPerTick, true) == energyPerTick) {
            energyOk = true;
        }
        return itemOk && fluidOk && energyOk;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull IInventory inventory) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    public ItemStack getInputStack() {
        return inputStack;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return output;
    }

    public ItemStack getResultItemAlt() {
        return outputAlt;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getEnergyPerTick() {
        return energyPerTick;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.WASHING_MACHINE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.WASHING_MACHINE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WashingMachineRecipe> {

        public Serializer() {
        }

        @Override
        public WashingMachineRecipe fromJson(ResourceLocation location, JsonObject json) {
            Item input = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.getAsJsonObject("input_item").get("item").getAsString()));
            int inputCount = json.getAsJsonObject("input_item").get("count").getAsInt();
            ItemStack inputStack = new ItemStack(input, inputCount);

            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(json.getAsJsonObject("input_fluid").get("fluid").getAsString()));
            int fluidAmount = json.getAsJsonObject("input_fluid").get("amount").getAsInt();
            assert fluid != null;
            FluidStack inputFluid = new FluidStack(fluid, fluidAmount);

            int processingTime = json.get("processing_time").getAsInt();
            int energyPerTick = json.get("energy_per_tick").getAsInt();

            Item output = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.getAsJsonObject("output").get("item").getAsString()));
            int outputCount = json.getAsJsonObject("output").get("count").getAsInt();
            ItemStack outputStack = new ItemStack(output, outputCount);

            ItemStack outputAltStack = ItemStack.EMPTY;
            if (json.has("output_alt")) {
                Item outputAlt = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.getAsJsonObject("output_alt").get("item").getAsString()));
                int outputAltCount = json.getAsJsonObject("output_alt").get("count").getAsInt();
                outputAltStack = new ItemStack(outputAlt, outputAltCount);
            }

            return new WashingMachineRecipe(location, inputStack, inputFluid, processingTime, energyPerTick, outputStack, outputAltStack);
        }

        @Nullable
        @Override
        public WashingMachineRecipe fromNetwork(ResourceLocation location, PacketBuffer packetBuffer) {
            ItemStack input = ItemStack.of(packetBuffer.readNbt());
            FluidStack fluid = FluidStack.readFromPacket(packetBuffer);
            int processingTime = packetBuffer.readInt();
            int energyPerTick = packetBuffer.readInt();
            ItemStack output = ItemStack.of(packetBuffer.readNbt());
            ItemStack outputAlt = ItemStack.of(packetBuffer.readNbt());
            return new WashingMachineRecipe(location, input, fluid, processingTime, energyPerTick, output, outputAlt);
        }

        @Override
        public void toNetwork(PacketBuffer packetBuffer, WashingMachineRecipe recipe) {
            packetBuffer.writeNbt(recipe.inputStack.serializeNBT());
            recipe.inputFluid.writeToPacket(packetBuffer);
            packetBuffer.writeInt(recipe.processingTime);
            packetBuffer.writeInt(recipe.energyPerTick);
            packetBuffer.writeNbt(recipe.output.serializeNBT());
            packetBuffer.writeNbt(recipe.outputAlt.serializeNBT());
        }

    }

    public static class Builder {

        private final WashingMachineRecipe recipe;

        public Builder(ResourceLocation id) {
            recipe = new WashingMachineRecipe(id);

        }

        public Builder withInput(IItemProvider input, int count) {
            recipe.inputStack = new ItemStack(input.asItem(), count);
            return this;
        }

        public Builder withInputFluid(FluidStack fluidStack) {
            recipe.inputFluid = fluidStack;
            return this;
        }

        public Builder withProcessingTime(int processingTime) {
            recipe.processingTime = processingTime;
            return this;
        }

        public Builder withEnergyPerTick(int energyPerTick) {
            recipe.energyPerTick = energyPerTick;
            return this;
        }

        public Builder withOutput(IItemProvider output, int count) {
            recipe.output = new ItemStack(output.asItem(), count);
            return this;
        }

        public Builder withOutputAlt(IItemProvider outputAlt, int count) {
            recipe.outputAlt = new ItemStack(outputAlt.asItem(), count);
            return this;
        }

        public void save(Consumer<IFinishedRecipe> consumer) {
            consumer.accept(new Result(this.recipe));
        }

    }

    public static class Result implements IFinishedRecipe {

        private final WashingMachineRecipe recipe;

        public Result(WashingMachineRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject inputItem = new JsonObject();
            inputItem.addProperty("item", recipe.inputStack.getItem().getRegistryName().toString());
            inputItem.addProperty("count", recipe.inputStack.getCount());
            json.add("input_item", inputItem);

            JsonObject inputFluid = new JsonObject();
            inputFluid.addProperty("fluid", recipe.inputFluid.getFluid().getRegistryName().toString());
            inputFluid.addProperty("amount", recipe.inputFluid.getAmount());
            json.add("input_fluid", inputFluid);

            json.addProperty("processing_time", recipe.processingTime);
            json.addProperty("energy_per_tick", recipe.energyPerTick);

            JsonObject outputItem = new JsonObject();
            outputItem.addProperty("item", recipe.output.getItem().getRegistryName().toString());
            outputItem.addProperty("count", recipe.output.getCount());
            json.add("output", outputItem);

            JsonObject outputAltItem = new JsonObject();
            outputAltItem.addProperty("item", recipe.outputAlt.getItem().getRegistryName().toString());
            outputAltItem.addProperty("count", recipe.outputAlt.getCount());
            json.add("output_alt", outputAltItem);
        }

        @Override
        public ResourceLocation getId() {
            return recipe.getId();
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return ModRecipeSerializers.WASHING_MACHINE.get();
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
