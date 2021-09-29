package net.mabbouxj.colorful_journey.world;

import net.mabbouxj.colorful_journey.blocks.ColorfulPortalBlock;
import net.mabbouxj.colorful_journey.data.tag.ModBlockTagProvider;
import net.mabbouxj.colorful_journey.events.BlockEventListeners;
import net.mabbouxj.colorful_journey.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.EntitySize;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class ColorfulPortalSize {

    private static final AbstractBlock.IPositionPredicate FRAME = (blockState, blockReader, blockPos) -> blockState.getBlock().is(ModBlockTagProvider.COLORFUL_PORTAL_FRAME);
    private final IWorld level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;

    public static Optional<ColorfulPortalSize> onTrySpawnPortal(IWorld world, BlockPos pos, Optional<ColorfulPortalSize> size) {
        if (!size.isPresent()) return size;
        return !MinecraftForge.EVENT_BUS.post(new BlockEventListeners.ColorfulPortalSpawnEvent(world, pos, world.getBlockState(pos), size.get())) ? size : Optional.empty();
    }

    public static Optional<ColorfulPortalSize> findEmptyPortalShape(IWorld world, BlockPos blockPos, Direction.Axis axis) {
        return findPortalShape(world, blockPos, (colorfulPortalSize) -> colorfulPortalSize.isValid() && colorfulPortalSize.numPortalBlocks == 0, axis);
    }

    public static Optional<ColorfulPortalSize> findPortalShape(IWorld world, BlockPos blockPos, Predicate<ColorfulPortalSize> predicate, Direction.Axis axis) {
        Optional<ColorfulPortalSize> optional = Optional.of(new ColorfulPortalSize(world, blockPos, axis)).filter(predicate);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new ColorfulPortalSize(world, blockPos, direction$axis)).filter(predicate);
        }
    }

    public ColorfulPortalSize(IWorld world, BlockPos pos, Direction.Axis direction) {
        this.level = world;
        this.axis = direction;
        this.rightDir = direction == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(pos);
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }

    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pos) {
        Direction leftDir = this.rightDir.getOpposite();

        BlockPos startPos = pos.relative(Direction.DOWN).relative(leftDir);
        int i = this.getDistanceUntilLeftEdgeFrame(startPos, leftDir) - 1;
        int j = this.getDistanceUntilBottomEdgeFrame(startPos.relative(leftDir, i)) - 1;
        if (i >= 0 && j >= 0)
            return startPos.relative(leftDir, i).relative(Direction.DOWN, j);

        startPos = pos.relative(Direction.DOWN).relative(this.rightDir);
        i = this.getDistanceUntilLeftEdgeFrame(startPos, leftDir) - 1;
        j = this.getDistanceUntilBottomEdgeFrame(startPos.relative(leftDir, i)) - 1;
        if (i >= 0 && j >= 0)
            return startPos.relative(leftDir, i).relative(Direction.DOWN, j);

        startPos = pos.relative(Direction.UP).relative(leftDir);
        i = this.getDistanceUntilLeftEdgeFrame(startPos, leftDir) - 1;
        j = this.getDistanceUntilBottomEdgeFrame(startPos.relative(leftDir, i)) - 1;
        if (i >= 0 && j >= 0)
            return startPos.relative(leftDir, i).relative(Direction.DOWN, j);

        startPos = pos.relative(Direction.UP).relative(this.rightDir);
        i = this.getDistanceUntilLeftEdgeFrame(startPos, leftDir) - 1;
        j = this.getDistanceUntilBottomEdgeFrame(startPos.relative(leftDir, i)) - 1;
        if (i >= 0 && j >= 0)
            return startPos.relative(leftDir, i).relative(Direction.DOWN, j);

        return null;
    }

    private int calculateWidth() {
        int i = this.getDistanceUntilLeftEdgeFrame(this.bottomLeft, this.rightDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private int getDistanceUntilLeftEdgeFrame(BlockPos pos, Direction direction) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i <= 21; ++i) {
            blockpos$mutable.set(pos).move(direction, i);
            BlockState blockstate = this.level.getBlockState(blockpos$mutable);
            if (!isEmpty(blockstate)) {
                if (FRAME.test(blockstate, this.level, blockpos$mutable)) {
                    return i;
                }
                break;
            }
        }

        return 0;
    }

    private int getDistanceUntilBottomEdgeFrame(BlockPos pos) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i <= 21; ++i) {
            blockpos$mutable.set(pos).move(Direction.DOWN, i);
            BlockState blockstate = this.level.getBlockState(blockpos$mutable);
            if (!isEmpty(blockstate)) {
                if (FRAME.test(blockstate, this.level, blockpos$mutable)) {
                    return i;
                }
                break;
            }
        }

        return 0;
    }

    private int calculateHeight() {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int i = this.getDistanceUntilTop(blockpos$mutable);
        return i >= 3 && i <= 21 && this.hasTopFrame(blockpos$mutable, i) ? i : 0;
    }

    private boolean hasTopFrame(BlockPos.Mutable pos, int p_242970_2_) {
        for(int i = 0; i < this.width; ++i) {
            BlockPos.Mutable blockpos$mutable = pos.set(this.bottomLeft).move(Direction.UP, p_242970_2_).move(this.rightDir, i);
            if (!FRAME.test(this.level.getBlockState(blockpos$mutable), this.level, blockpos$mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.Mutable pos) {
        for(int i = 0; i < 21; ++i) {
            pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!FRAME.test(this.level.getBlockState(pos), this.level, pos)) {
                return i;
            }

            pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.getBlockState(pos), this.level, pos)) {
                return i;
            }

            for(int j = 0; j < this.width; ++j) {
                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(pos);
                if (!isEmpty(blockstate)) {
                    return i;
                }

                if (blockstate.is(ModBlocks.COLORFUL_PORTAL.get())) {
                    ++this.numPortalBlocks;
                }
            }
        }

        return 21;
    }

    private static boolean isEmpty(BlockState state) {
        return state.is(Blocks.AIR) || state.is(ModBlocks.COLORFUL_PORTAL.get());
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState blockstate = ModBlocks.COLORFUL_PORTAL.get().defaultBlockState().setValue(ColorfulPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach((pos) -> {
            this.level.setBlock(pos, blockstate, 18);
        });
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vector3d getRelativePosition(TeleportationRepositioner.Result result, Direction.Axis axis, Vector3d vector3d, EntitySize entitySize) {
        double d0 = (double)result.axis1Size - (double)entitySize.width;
        double d1 = (double)result.axis2Size - (double)entitySize.height;
        BlockPos blockpos = result.minCorner;
        double d2;
        if (d0 > 0.0D) {
            float f = (float)blockpos.get(axis) + entitySize.width / 2.0F;
            d2 = MathHelper.clamp(MathHelper.inverseLerp(vector3d.get(axis) - (double)f, 0.0D, d0), 0.0D, 1.0D);
        } else {
            d2 = 0.5D;
        }

        double d4;
        if (d1 > 0.0D) {
            Direction.Axis direction$axis = Direction.Axis.Y;
            d4 = MathHelper.clamp(MathHelper.inverseLerp(vector3d.get(direction$axis) - (double)blockpos.get(direction$axis), 0.0D, d1), 0.0D, 1.0D);
        } else {
            d4 = 0.0D;
        }

        Direction.Axis direction$axis1 = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double d3 = vector3d.get(direction$axis1) - ((double)blockpos.get(direction$axis1) + 0.5D);
        return new Vector3d(d2, d4, d3);
    }

    public static PortalInfo createPortalInfo(ServerWorld serverWorld, TeleportationRepositioner.Result result, Direction.Axis axis, Vector3d p_242963_3_, EntitySize entitySize, Vector3d p_242963_5_, float p_242963_6_, float p_242963_7_) {
        BlockPos blockpos = result.minCorner;
        BlockState blockstate = serverWorld.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
        double d0 = (double)result.axis1Size;
        double d1 = (double)result.axis2Size;
        int i = axis == direction$axis ? 0 : 90;
        Vector3d vector3d = axis == direction$axis ? p_242963_5_ : new Vector3d(p_242963_5_.z, p_242963_5_.y, -p_242963_5_.x);
        double d2 = (double)entitySize.width / 2.0D + (d0 - (double)entitySize.width) * p_242963_3_.x();
        double d3 = (d1 - (double)entitySize.height) * p_242963_3_.y();
        double d4 = 0.5D + p_242963_3_.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vector3d vector3d1 = new Vector3d((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        return new PortalInfo(vector3d1, vector3d, p_242963_6_ + (float)i, p_242963_7_);
    }

}
