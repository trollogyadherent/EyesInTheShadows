package trollogyadherent.eyesintheshadows.packet.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.client.JumpscareOverlay;

public class InitiateJumpscarePacket implements IMessageHandler<InitiateJumpscarePacket.SimpleMessage, IMessage> {

    @Override
    public IMessage onMessage(InitiateJumpscarePacket.SimpleMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            EyesInTheShadows.debug("Received InitiateJumpscarePacket packet");
            JumpscareOverlay.INSTANCE.show(message.px, message.py, message.pz);
        }
        return null;
    }

    public static class SimpleMessage implements IMessage {
        public double px;
        public double py;
        public double pz;

        // this constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
        public SimpleMessage() {}

        public SimpleMessage(double px, double py, double pz)
        {
            this.px = px;
            this.py = py;
            this.pz = pz;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            px = buf.readDouble();
            py = buf.readDouble();
            pz = buf.readDouble();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeDouble(px);
            buf.writeDouble(py);
            buf.writeDouble(pz);
        }
    }
}