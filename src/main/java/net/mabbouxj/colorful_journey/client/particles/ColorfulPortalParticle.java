package net.mabbouxj.colorful_journey.client.particles;

import net.mabbouxj.colorful_journey.utils.ColorUtils;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColorfulPortalParticle extends SpriteTexturedParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;

    protected ColorfulPortalParticle(ClientWorld world, double x, double y, double z, double xd, double yd, double yz) {
        super(world, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = yz;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        DyeColor color = ColorUtils.getRandomEnabledColor();
        this.rCol = color.getTextureDiffuseColors()[0];
        this.gCol = color.getTextureDiffuseColors()[1];
        this.bCol = color.getTextureDiffuseColors()[2];
        this.lifetime = (int)(Math.random() * 10.0D) + 40;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float p_217561_1_) {
        float f = ((float)this.age + p_217561_1_) / (float)this.lifetime;
        f = 1.0F - f;
        f = f * f;
        f = 1.0F - f;
        return this.quadSize * f;
    }

    public int getLightColor(float p_189214_1_) {
        int i = super.getLightColor(p_189214_1_);
        float f = (float)this.age / (float)this.lifetime;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int)(f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / (float)this.lifetime;
            float f1 = -f + f * f * 2.0F;
            float f2 = 1.0F - f1;
            this.x = this.xStart + this.xd * (double)f2;
            this.y = this.yStart + this.yd * (double)f2 + (double)(1.0F - f);
            this.z = this.zStart + this.zd * (double)f2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BasicParticleType particleType, ClientWorld world, double x, double y, double z, double xd, double yd, double zd) {
            ColorfulPortalParticle portalparticle = new ColorfulPortalParticle(world, x, y, z, xd, yd, zd);
            portalparticle.pickSprite(this.sprite);
            return portalparticle;
        }
    }
}