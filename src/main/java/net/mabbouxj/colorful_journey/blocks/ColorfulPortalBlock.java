package net.mabbouxj.colorful_journey.blocks;

import net.mabbouxj.colorful_journey.init.ModParticles;
import net.mabbouxj.colorful_journey.init.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ColorfulPortalBlock extends NetherPortalBlock {

    public ColorfulPortalBlock() {
        super(AbstractBlock.Properties.of(Material.PORTAL).noCollission().randomTicks().strength(-1.0F).sound(SoundType.WOOL).lightLevel((lvl) -> 15));
    }

    @Override
    public void randomTick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random rand) {
        // Spawn mob ?
    }

    @Override
    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        // TP somewhere
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random rand) {
        if (rand.nextInt(100) == 0) {
            world.playLocalSound((double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, ModSounds.COLORFUL_PORTAL_AMBIENT.get(), SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double x = (double)blockPos.getX() + rand.nextDouble();
            double y = (double)blockPos.getY() + rand.nextDouble();
            double z = (double)blockPos.getZ() + rand.nextDouble();
            double xd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double yd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double zd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;
            if (!world.getBlockState(blockPos.west()).is(this) && !world.getBlockState(blockPos.east()).is(this)) {
                x = (double)blockPos.getX() + 0.5D + 0.25D * (double)j;
                xd = (rand.nextFloat() * 2.0F * (float)j);
            } else {
                z = (double)blockPos.getZ() + 0.5D + 0.25D * (double)j;
                zd = (rand.nextFloat() * 2.0F * (float)j);
            }

            world.addParticle(ModParticles.COLORFUL_PORTAL.get(), x, y, z, xd, yd, zd);
        }

    }

}
