/* Based on: https://github.com/jabelar/WildAnimalsPlus-1.7.10/blob/master/src/main/java/com/blogspot/jabelarminecraft/wildanimals/networking/MessageSyncEntityToClient.java */
/* Now you know what it is based on */

package trollogyadherent.eyesintheshadows.packet.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.entity.IModEntity;

public class MessageSyncEntityToClient implements IMessage
{
    private int entityId ;
    private NBTTagCompound entitySyncDataCompound;

    public MessageSyncEntityToClient() {
        // need this constructor
    }

    public MessageSyncEntityToClient(int parEntityId, NBTTagCompound parTagCompound) {
        entityId = parEntityId;
        entitySyncDataCompound = parTagCompound;
//        // DEBUG
//        System.out.println("SyncEntityToClient constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = ByteBufUtils.readVarInt(buf, 4);
        entitySyncDataCompound = ByteBufUtils.readTag(buf); // this class is very useful in general for writing more complex objects
//    	// DEBUG
//    	System.out.println("fromBytes");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, entityId, 4);
        ByteBufUtils.writeTag(buf, entitySyncDataCompound);
//        // DEBUG
//        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageSyncEntityToClient, IMessage> {

        @Override
        public IMessage onMessage(MessageSyncEntityToClient message, MessageContext ctx) {
            EntityPlayer thePlayer = EyesInTheShadows.proxy.getPlayerEntityFromContext(ctx);
            IModEntity theEntity = (IModEntity)thePlayer.worldObj.getEntityByID(message.entityId);
            if (theEntity != null) {
                theEntity.setSyncDataCompound(message.entitySyncDataCompound);
                //EyesInTheShadows.debug("MessageSyncEnitityToClient onMessage(), entity ID = " + message.entityId); //spams way too often
            } else {
                EyesInTheShadows.debug("Can't find entity with ID = " + message.entityId + " on client");
            }
            return null; // no response in this case
        }
    }
}