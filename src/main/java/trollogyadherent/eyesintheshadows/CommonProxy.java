package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;
import trollogyadherent.eyesintheshadows.packet.PacketHandler;
import trollogyadherent.eyesintheshadows.util.Util;
import trollogyadherent.eyesintheshadows.varinstances.VarInstanceServer;

public class CommonProxy {
    protected int modEntityID = -1;

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) 	{
        if (Util.isServer()) {
            EyesInTheShadows.varInstanceServer = new VarInstanceServer();
        }

        EyesInTheShadows.confFile = event.getSuggestedConfigurationFile();
        if (Util.isServer()) {
            Config.synchronizeConfigurationServer(event.getSuggestedConfigurationFile(), false);
        } else {
            Config.synchronizeConfigurationClient(event.getSuggestedConfigurationFile(), false, true);
        }
        EyesInTheShadows.debug("I am " + Tags.MODNAME + " at version " + Tags.VERSION + " and group name " + Tags.GROUPNAME);

        PacketHandler.initPackets();

        EntityRegistry.registerModEntity(EntityEyes.class, "Eyes", ++ modEntityID, EyesInTheShadows.instance, 80, 3, false);
        Util.registerSpawnEgg("Eyes", 0x000000, 0x7F0000);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {

    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStarted(FMLServerStartedEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public void serverStopped(FMLServerStoppedEvent event) {

    }

    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }
}
