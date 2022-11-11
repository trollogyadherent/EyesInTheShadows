package trollogyadherent.eyesintheshadows.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import org.lwjgl.input.Keyboard;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

public class EventHandler {
    int lastPressedKey;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void ontick(TickEvent.PlayerTickEvent e) {
        if (EyesInTheShadows.isDebugMode()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
                lastPressedKey = Keyboard.KEY_R;
            } else if (lastPressedKey == Keyboard.KEY_R) {
                lastPressedKey = -1;

                int x = MathHelper.floor_double(e.player.posX) + EyesInTheShadows.varInstanceClient.xmod;
                int y = MathHelper.floor_double(e.player.posY) + EyesInTheShadows.varInstanceClient.ymod;
                int z = MathHelper.floor_double(e.player.posZ) + EyesInTheShadows.varInstanceClient.zmod;
                Block b = e.player.getEntityWorld().getBlock(x, y, z);
                EyesInTheShadows.debug("Block name: " + b.getLocalizedName() + ", x: " + x + ", y: " + y + ", z :" + z);
                EyesInTheShadows.debug("Block lightvalue: " + b.getLightValue());
                /* seems like the winner */ EyesInTheShadows.debug("world.getBlockLightValue: " + e.player.getEntityWorld().getBlockLightValue(x, y, z));
                EyesInTheShadows.debug("world.getBlockLightValue_do: " + e.player.getEntityWorld().getBlockLightValue_do(x, y, z, false));
                EyesInTheShadows.debug("world.getFullBlockLightValue: " + e.player.getEntityWorld().getFullBlockLightValue(x, y, z));
                EyesInTheShadows.debug("world.getLightBrightness: " + e.player.getEntityWorld().getLightBrightness(x, y, z));
                EyesInTheShadows.debug("world.getSavedLightValue: " + e.player.getEntityWorld().getSavedLightValue(EnumSkyBlock.Block, x, y, z));
                EyesInTheShadows.debug("b.getLightOpacity: " + b.getLightOpacity());
                EyesInTheShadows.debug("===================eventhandler==================");
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
                lastPressedKey = Keyboard.KEY_J;
            } else if (lastPressedKey == Keyboard.KEY_J) {
                lastPressedKey = -1;

                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.xmod += 1;
                } else {
                    EyesInTheShadows.varInstanceClient.xmod -= 1;
                }
                EyesInTheShadows.debug("xmod: " + EyesInTheShadows.varInstanceClient.xmod);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
                lastPressedKey = Keyboard.KEY_K;
            } else if (lastPressedKey == Keyboard.KEY_K) {
                lastPressedKey = -1;

                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.ymod += 1;
                } else {
                    EyesInTheShadows.varInstanceClient.ymod -= 1;
                }
                EyesInTheShadows.debug("ymod: " + EyesInTheShadows.varInstanceClient.ymod);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
                lastPressedKey = Keyboard.KEY_L;
            } else if (lastPressedKey == Keyboard.KEY_L) {
                lastPressedKey = -1;

                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.zmod += 1;
                } else {
                    EyesInTheShadows.varInstanceClient.zmod -= 1;
                }
                EyesInTheShadows.debug("zmod: " + EyesInTheShadows.varInstanceClient.zmod);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                lastPressedKey = Keyboard.KEY_Z;
            } else if (lastPressedKey == Keyboard.KEY_Z) {
                lastPressedKey = -1;

                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.renderpass += 1;
                } else {
                    EyesInTheShadows.varInstanceClient.renderpass -= 1;
                }
                EyesInTheShadows.debug("renderpass: " + EyesInTheShadows.varInstanceClient.renderpass);
            }


            if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                lastPressedKey = Keyboard.KEY_1;
            } else if (lastPressedKey == Keyboard.KEY_1) {
                lastPressedKey = -1;
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.hmod -= 0.01;
                } else {
                    EyesInTheShadows.varInstanceClient.hmod += 0.01;
                }
                EyesInTheShadows.debug("hmod: " + EyesInTheShadows.varInstanceClient.hmod);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                lastPressedKey = Keyboard.KEY_2;
            } else if (lastPressedKey == Keyboard.KEY_2) {
                lastPressedKey = -1;
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    EyesInTheShadows.varInstanceClient.hmod2 -= 0.1;
                } else {
                    EyesInTheShadows.varInstanceClient.hmod2 += 0.1;
                }
                EyesInTheShadows.debug("hmod2: " + EyesInTheShadows.varInstanceClient.hmod2);
            }
        }
    }
}
