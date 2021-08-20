package net.mabbouxj.colorful_journey.client.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mabbouxj.colorful_journey.init.ModParticles;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Locale;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class InkSplashParticle extends SpriteTexturedParticle {

    private final int MIN_LIFE_TIME = 20;
    private final int MAX_LIFE_TIME = 40;
    final double GRAVITY_ACCELERATION_PER_TICK = -0.04;
    private final IAnimatedSprite sprites;

    protected InkSplashParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, IAnimatedSprite sprites, Color color, double diameter) {
        super(world, x, y, z);
        this.sprites = sprites;

        setColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        setSize((float) diameter, (float) diameter);

        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;

        this.lifetime = new Random().nextInt(MAX_LIFE_TIME - MIN_LIFE_TIME) + MIN_LIFE_TIME;
        this.hasPhysics = true;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);

            this.yd += GRAVITY_ACCELERATION_PER_TICK;
            this.move(this.xd, this.yd, this.zd);

            this.xd *= 0.9F;
            this.yd *= 0.9F;
            this.zd *= 0.9F;

            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<InkSplashParticle.Data> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite animatedSprite) {
            this.sprites = animatedSprite;
        }

        public Particle createParticle(InkSplashParticle.Data particleData, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            InkSplashParticle particle = new InkSplashParticle(world, x, y, z, velocityX, velocityY, velocityZ, sprites, particleData.getColor(), particleData.getDiameter());
            particle.pickSprite(sprites);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Data implements IParticleData {

        private Color color;
        private double diameter;

        public static final Codec<InkSplashParticle.Data> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT.fieldOf("color").forGetter(d -> d.color.getRGB()),
                        Codec.DOUBLE.fieldOf("diameter").forGetter(d -> d.diameter)
                ).apply(instance, InkSplashParticle.Data::new)
        );

        public Data(Color color, double diameter) {
            this.color = color;
            this.diameter = diameter;
        }

        private Data(int colorRGB, double diameter) {
            this.color = new Color(colorRGB);
            this.diameter = constrainDiameterToValidRange(diameter);
        }

        private static double constrainDiameterToValidRange(double diameter) {
            final double MIN_DIAMETER = 0.05;
            final double MAX_DIAMETER = 1.0;
            return MathHelper.clamp(diameter, MIN_DIAMETER, MAX_DIAMETER);
        }

        public Color getColor() {
            return color;
        }

        public double getDiameter() {
            return diameter;
        }

        @Override
        public ParticleType<InkSplashParticle.Data> getType() {
            return ModParticles.INK_SPLASH.get();
        }

        @Override
        public void writeToNetwork(PacketBuffer buf) {
            buf.writeInt(color.getRed());
            buf.writeInt(color.getGreen());
            buf.writeInt(color.getBlue());
            buf.writeDouble(diameter);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %i %i %i", this.getType().getRegistryName(), diameter, color.getRed(), color.getGreen(), color.getBlue());
        }

        public static final IDeserializer<InkSplashParticle.Data> DESERIALIZER = new IDeserializer<Data>() {

            // parse the parameters for this particle from a /particle command
            @Nonnull
            @Override
            public InkSplashParticle.Data fromCommand(@Nonnull ParticleType<InkSplashParticle.Data> type, @Nonnull StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                double diameter = constrainDiameterToValidRange(reader.readDouble());

                final int MIN_COLOUR = 0;
                final int MAX_COLOUR = 255;
                reader.expect(' ');
                int red = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
                reader.expect(' ');
                int green = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
                reader.expect(' ');
                int blue = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
                Color color = new Color(red, green, blue);

                return new InkSplashParticle.Data(color, diameter);
            }

            // read the particle information from a PacketBuffer after the client has received it from the server
            @Override
            public InkSplashParticle.Data fromNetwork(@Nonnull ParticleType<InkSplashParticle.Data> type, PacketBuffer buf) {
                // warning! never trust the data read in from a packet buffer.
                final int MIN_COLOUR = 0;
                final int MAX_COLOUR = 255;
                int red = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
                int green = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
                int blue = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
                Color color = new Color(red, green, blue);

                double diameter = constrainDiameterToValidRange(buf.readDouble());

                return new InkSplashParticle.Data(color, diameter);
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static class Type extends ParticleType<InkSplashParticle.Data> {
        private static boolean ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER = false;

        public Type() {
            super(ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER, InkSplashParticle.Data.DESERIALIZER);
        }

        // get the Codec used to
        // a) convert a Data to a serialised format
        // b) construct a Data object from the serialised format
        public Codec<InkSplashParticle.Data> codec() {
            return InkSplashParticle.Data.CODEC;
        }
    }
}
