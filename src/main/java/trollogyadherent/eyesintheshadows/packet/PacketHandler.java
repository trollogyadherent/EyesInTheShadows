package trollogyadherent.eyesintheshadows.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.packet.packets.InitiateJumpscarePacket;
import trollogyadherent.eyesintheshadows.packet.packets.MessageSyncEntityToClient;

public class PacketHandler {
    public static SimpleNetworkWrapper net;

    public static void initPackets()
    {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID.toUpperCase());
        registerMessage(InitiateJumpscarePacket.class, InitiateJumpscarePacket.SimpleMessage.class);
        registerMessage(MessageSyncEntityToClient.Handler.class, MessageSyncEntityToClient.class);
    }

    private static int nextPacketId = 0;

    private static void registerMessage(Class packet, Class message)
    {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }
}
