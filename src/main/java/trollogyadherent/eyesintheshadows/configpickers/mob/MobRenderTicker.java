package trollogyadherent.eyesintheshadows.configpickers.mob;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.configpickers.entityrenderutil.EntityUtils;
import trollogyadherent.eyesintheshadows.configpickers.entityrenderutil.FakeWorld;

import java.util.*;

@SideOnly(Side.CLIENT)
public class MobRenderTicker
{
    private static Minecraft mcClient;
    private static boolean isRegistered = false;
    private static World world;
    @SuppressWarnings("unused")
    private GuiScreen savedScreen;
    private static List<ItemStack> playerItems = new ArrayList<>();
    private static Random random = new Random();

    private static Set entities;
    private static Object[] entStrings;
    private static List entityBlacklist;
    private static int id;

    private static boolean erroredOut = false;

    public static EntityClientPlayerMP clientPlayerMP = null;

    public static float x = 0;
    public static float y = 1;
    public static float z = 0.03F;

    private static EntityLivingBase mob;

    public MobRenderTicker() {
        mcClient = FMLClientHandler.instance().getClient();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
       if (!erroredOut && (mcClient.currentScreen instanceof MobSelectionGui)) {
            try {
                if (world == null) {
                    EyesInTheShadows.debug("world null, initializing");
                    init();
                }
                if ((world != null) && (mob != null)) {
                    ScaledResolution sr = new ScaledResolution(mcClient, mcClient.displayWidth, mcClient.displayHeight);
                    final int mouseX = (Mouse.getX() * sr.getScaledWidth()) / mcClient.displayWidth;
                    final int mouseY = sr.getScaledHeight() - ((Mouse.getY() * sr.getScaledHeight()) / mcClient.displayHeight) - 1;
                    int distanceToSide = ((mcClient.currentScreen.width / 2) + 98);
                    float targetHeight = (float) (sr.getScaledHeight_double() / 5.0F) / 1.8F;
                    float scale = EntityUtils.getEntityScale(mob, targetHeight, 1.8F);
                    EntityUtils.drawEntityOnScreenFollowMouse(
                            distanceToSide,
                            (int) ((sr.getScaledHeight() / 2) + (mob.height * scale)),
                            scale,
                            distanceToSide - mouseX,
                            ((sr.getScaledHeight() / 2) + (mob.height * scale)) - (mob.height * scale * (mob.getEyeHeight() / mob.height)) - mouseY,
                            mob);
                }
            } catch (Throwable e) {
                EyesInTheShadows.error("Main menu mob rendering encountered a serious error and has been disabled for the remainder of this session.");
                e.printStackTrace();
                erroredOut = true;
                mcClient.thePlayer = null;
                mob = null;
                world = null;
            }
        }
    }

    private void init() {
        try {
            boolean createNewWorld = world == null;

            if (createNewWorld) {
                world = new FakeWorld();
            }

            if (createNewWorld || (mcClient.thePlayer == null))
            {
                mcClient.thePlayer = new EntityClientPlayerMP(mcClient, world, mcClient.getSession(), null, null);
                mcClient.thePlayer.dimension = 0;
                mcClient.thePlayer.movementInput = new MovementInputFromOptions(mcClient.gameSettings);
                mcClient.thePlayer.eyeHeight = 1.82F;
                //setRandomMobItem(mcClient.thePlayer);
            }

            /*if (createNewWorld || (mobEntityLivingBase == null))
            {
                if (bspkrsCoreMod.instance.allowDebugOutput)
                {
                    mobEntityLivingBase = getNextEntity(world);
                }
                else
                {
                    mobEntityLivingBase = EntityUtils.getRandomLivingEntity(world, entityBlacklist, 4, fallbackPlayerNames);
                }
            }*/

            RenderManager.instance.cacheActiveRenderInfo(world, mcClient.renderEngine, mcClient.fontRenderer, mcClient.thePlayer, mcClient.thePlayer, mcClient.gameSettings, 0.0F);
            savedScreen = mcClient.currentScreen;
        } catch (Throwable e) {
            EyesInTheShadows.error("Main menu mob rendering encountered a serious error and has been disabled for the remainder of this session.");
            e.printStackTrace();
            erroredOut = true;
            mcClient.thePlayer = null;
            mob = null;
            world = null;
        }
    }

    public static void setMob(String mobString) {
        if (world == null) {
            EyesInTheShadows.debug("(setMob) world null");
            mob = null;
            return;
        }
        Entity entity = EntityList.createEntityByName(mobString, world);
        if (!(entity instanceof EntityLivingBase)) {
            EyesInTheShadows.debug("(setMob) failed to create entity from \"" + mobString + "\"");
            mob = null;
            return;
        }

        mob = (EntityLivingBase) entity;
    }

    private static EntityLivingBase getNextEntity(World world) {
        Class clazz;
        int tries = 0;
        do
        {
            if (++id >= entStrings.length)
                id = 0;
            clazz = (Class) EntityList.stringToClassMapping.get(entStrings[id]);
        }
        while (!EntityLivingBase.class.isAssignableFrom(clazz) && (++tries <= 5));

        return (EntityLivingBase) EntityList.createEntityByName((String) entStrings[id], world);
    }

    private static void setRandomMobItem(EntityLivingBase ent) {
        try
        {
            if (ent instanceof AbstractClientPlayer) {
                ent.setCurrentItemOrArmor(0, playerItems.get(random.nextInt(playerItems.size())));
            }
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    public void register() {
        if (!isRegistered)
        {
            EyesInTheShadows.info("Enabling mob render ticker");
            FMLCommonHandler.instance().bus().register(this);
            isRegistered = true;
        }
    }

    public void unRegister() {
        if (isRegistered)
        {
            EyesInTheShadows.info("Disabling mob render ticker");
            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
            world = null;
            clientPlayerMP = null;
        }
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    static {
        entityBlacklist = new ArrayList();
        entityBlacklist.add("Mob");
        entityBlacklist.add("Monster");

        // Get a COPY dumbass!
        entities = new TreeSet(EntityList.stringToClassMapping.keySet());
        entStrings = entities.toArray(new Object[] {});
        id = -1;
    }
}