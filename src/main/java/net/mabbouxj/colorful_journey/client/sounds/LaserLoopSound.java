package net.mabbouxj.colorful_journey.client.sounds;

import net.mabbouxj.colorful_journey.init.ModSounds;
import net.mabbouxj.colorful_journey.items.ColorLaserGunItem;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class LaserLoopSound extends TickableSound {

    private final PlayerEntity player;
    private float distance = 0.0F;

    public LaserLoopSound(PlayerEntity player, float volume) {
        super(ModSounds.LASER_LOOP.get(), SoundCategory.PLAYERS);
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.x = (float) player.getX();
        this.y = (float) player.getY();
        this.z = (float) player.getZ();
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        ItemStack heldItem = ColorLaserGunItem.getGun(player);
        if (!(this.player.isUsingItem() && heldItem.getItem() instanceof ColorLaserGunItem)) {
            this.stop();
        } else {
            this.x = (float) this.player.getX();
            this.y = (float) this.player.getY();
            this.z = (float) this.player.getZ();
        }
    }

}
