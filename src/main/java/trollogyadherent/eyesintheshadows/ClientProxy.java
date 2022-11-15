package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import trollogyadherent.eyesintheshadows.event.ClientEventHandler;
import trollogyadherent.eyesintheshadows.client.EyesRenderer;
import trollogyadherent.eyesintheshadows.client.ModelEyes;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;
import trollogyadherent.eyesintheshadows.varinstances.VarInstanceClient;

public class ClientProxy extends CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) 	{
        EyesInTheShadows.varInstanceClient = new VarInstanceClient();

        RenderingRegistry.registerEntityRenderingHandler(EntityEyes.class, new EyesRenderer(new ModelEyes(), 0.5F));

        ClientEventHandler clientEventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        FMLCommonHandler.instance().bus().register(clientEventHandler);
        super.preInit(event);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        super.serverAboutToStart(event);
    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        super.serverStarted(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        super.serverStopping(event);
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        super.serverStopped(event);
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work because you will be getting a client
        // player even when you are on the server! Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntityFromContext(ctx));
    }
}