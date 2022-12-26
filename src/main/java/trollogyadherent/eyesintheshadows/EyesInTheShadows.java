package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.apache.logging.log4j.Logger;
import trollogyadherent.eyesintheshadows.varinstances.VarInstanceClient;
import trollogyadherent.eyesintheshadows.varinstances.VarInstanceCommon;
import trollogyadherent.eyesintheshadows.varinstances.VarInstanceServer;

import java.io.File;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.7.10]", guiFactory = "trollogyadherent."+ Tags.MODID +".gui.GuiFactory")
public class EyesInTheShadows {
    @Mod.Instance(Tags.MODID)
    public static EyesInTheShadows instance;

    private static Logger LOG;
    public static VarInstanceClient varInstanceClient;
    public static VarInstanceServer varInstanceServer;
    public static VarInstanceCommon varInstanceCommon;
    private static boolean DEBUG_MODE;
    public static String rootPath = Tags.MODID;
    public static final int maxPngDimension = 2500;
    public static File confFile;

    @SidedProxy(clientSide= Tags.GROUPNAME + ".ClientProxy", serverSide=Tags.GROUPNAME + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();
        String debugVar = System.getenv("MCMODDING_DEBUG_MODE");
        DEBUG_MODE = debugVar != null;
        EyesInTheShadows.info("Debugmode: " + DEBUG_MODE);


        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        proxy.serverAboutToStart(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        proxy.serverStopped(event);
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE || Config.debugMode;
    }

    public static void debug(String message) {
        if (isDebugMode()) {
            LOG.info("DEBUG: " + message);
        }
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }
}