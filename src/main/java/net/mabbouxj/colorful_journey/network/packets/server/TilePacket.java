package net.mabbouxj.colorful_journey.network.packets.server;

import io.netty.buffer.Unpooled;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.network.packets.IPacketServer;
import net.mabbouxj.colorful_journey.network.packets.PacketBase;
import net.mabbouxj.colorful_journey.tiles.BasicTile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TilePacket extends PacketBase implements IPacketServer {

    protected BlockPos pos;
    protected PacketBuffer buffer;

    public TilePacket() {
        super(0, ColorfulJourney.PACKET_HANDLER);
    }

    @Override
    public void handleServer(ServerPlayerEntity player) {
        World world = player.level;
        if (!world.isLoaded(pos)) {
            return;
        }
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof BasicTile) {
            ((BasicTile) tile).handleTilePacket(buffer);
        }
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeBytes(buffer);
    }

    @Override
    public void read(PacketBuffer buf) {
        buffer = buf;
        pos = buffer.readBlockPos();
    }

    public static void sendToServer(BasicTile tile) {
        TilePacket packet = new TilePacket();
        packet.pos = tile.getBlockPos();
        packet.buffer = tile.getTilePacket(new PacketBuffer(Unpooled.buffer()));
        packet.sendToServer();
    }

}
