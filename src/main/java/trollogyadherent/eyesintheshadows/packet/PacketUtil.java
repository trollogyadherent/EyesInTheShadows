package trollogyadherent.eyesintheshadows.packet;

import net.minecraft.entity.Entity;
import trollogyadherent.eyesintheshadows.entity.IModEntity;
import trollogyadherent.eyesintheshadows.packet.packets.MessageSyncEntityToClient;

public class PacketUtil {

    public static void sendEntitySyncPacketToClient(IModEntity parEntity) {
        Entity theEntity = (Entity)parEntity;
        if (!theEntity.worldObj.isRemote)
        {
//        	// DEBUG
//        	System.out.println("sendEntitySyncPacket from server");
            PacketHandler.net.sendToAll(new MessageSyncEntityToClient(theEntity.getEntityId(), parEntity.getSyncDataCompound()));
        }

    }
}
