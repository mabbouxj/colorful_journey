package net.mabbouxj.colorful_journey.tiles;


import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.annotations.Sync;
import net.mabbouxj.colorful_journey.components.InventoryComponent;
import net.mabbouxj.colorful_journey.containers.EaselContainer;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.mabbouxj.colorful_journey.init.ModTiles;
import net.mabbouxj.colorful_journey.network.packets.server.TilePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EaselTile extends BasicTile implements INamedContainerProvider {

    public static final ResourceLocation EMPTY_SLATE_TEX = new ResourceLocation(ColorfulJourney.MOD_ID, "textures/block/easel_slate.png");
    private final List<DyeColor> POSSIBLE_DYES = Arrays.asList(
            DyeColor.RED, DyeColor.GREEN, DyeColor.BLUE,
            DyeColor.MAGENTA, DyeColor.CYAN, DyeColor.YELLOW,
            DyeColor.WHITE, DyeColor.PURPLE, DyeColor.ORANGE
    );
    private final List<ResourceLocation> PAINTS = Arrays.asList(
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/bob_ross.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/peaceful_valley.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/tropical_seascape.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/sunset_aglow.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/mountain_summit.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/ocean_sunrise.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/northern_lights.png"),
            new ResourceLocation(ColorfulJourney.MOD_ID, "textures/painting/evenings_glow.png")
    );

    public enum Slots {
        PALETTE(0, (stack) -> { return stack.getItem().equals(ModItems.COLOR_PALETTE.get()); });

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
    public boolean hasSlate = false;
    @Sync
    public int currentPaint = generateRandomPaintTexture();
    @Sync
    public int selectedColor = DyeColor.RED.getId();
    @Sync
    public int[] patternToReproduce = generateRandomPattern();
    @Sync
    public int[] patternBeingPainted = initPattern();
    @Sync
    public InventoryComponent inventoryStorage;
    private LazyOptional<ItemStackHandler> inventory;

    public EaselTile() {
        super(ModTiles.EASEL.get());
        this.inventoryStorage = new InventoryComponent(this, EaselTile.Slots.values().length);
        this.inventory = LazyOptional.of(() -> inventoryStorage);
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
        assert level != null;
        return new EaselContainer(this, i, playerInventory, this.inventory.orElse(new ItemStackHandler(EaselTile.Slots.values().length)));
    }

    public boolean popSlate(World world, BlockPos pos) {
        if (!hasSlate || this.level == null)
            return false;
        ItemStack painting = new ItemStack(Items.PAPER, 1);
        if (isFinished()) {
            painting = new ItemStack(ModItems.FINISHED_PAINTING.get(), 1);
            this.level.addFreshEntity(new ExperienceOrbEntity(this.level, pos.getX(), pos.getY(), pos.getZ(), 5 + new Random().nextInt(20)));
        }
        InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), painting);
        hasSlate = false;
        currentPaint = generateRandomPaintTexture();
        selectedColor = DyeColor.RED.getId();
        patternToReproduce = generateRandomPattern();
        patternBeingPainted = initPattern();
        return true;
    }

    private int generateRandomPaintTexture() {
        return new Random().nextInt(PAINTS.size());
    }

    public ResourceLocation getPaintTexture() {
        return PAINTS.get(currentPaint);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.colorful_journey.easel");
    }

    private int[] generateRandomPattern() {
        int[] result = initPattern();
        for (int i = 0; i < 9; i++) {
            result[i] = POSSIBLE_DYES.get(new Random().nextInt(POSSIBLE_DYES.size())).getId();
        }
        return result;
    }

    private int[] initPattern() {
        return new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
    }

    public boolean isEmpty() {
        return Arrays.stream(patternBeingPainted).allMatch((x) -> x < 0);
    }

    public boolean isFinished() {
        return Arrays.equals(patternBeingPainted, patternToReproduce);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide)
            TilePacket.sendToServer(this);
        if (isFinished()) {
            if (this.level.isClientSide && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.closeContainer();
            } else if (!this.level.isClientSide) {
                this.level.playSound(null, this.getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(cap, side);
    }
}
