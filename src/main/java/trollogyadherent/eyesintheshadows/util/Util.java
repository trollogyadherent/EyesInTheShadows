package trollogyadherent.eyesintheshadows.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import de.matthiasmann.twl.utils.PNGDecoder;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.spawnegg.EyesMonsterPlacer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static boolean isServer() {
        return FMLCommonHandler.instance().getSide() == Side.SERVER;
    }

    public static byte[] fillByteArrayLeading(byte[] a, int totalLen) throws IOException {
        if (a.length >= totalLen) {
            return a;
        }
        byte[] b = new byte[totalLen - a.length];
        return concatByteArrays(b, a);
    }

    public static boolean pngIsSane(File imageFile) {
        try {
            PNGDecoder pngDecoder = new PNGDecoder(Files.newInputStream(imageFile.toPath()));
            if (pngDecoder.getWidth() > EyesInTheShadows.maxPngDimension || pngDecoder.getHeight() > EyesInTheShadows.maxPngDimension) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static byte[] concatByteArrays(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(a);
        outputStream.write(b);

        return outputStream.toByteArray();
    }

    public static byte[] intToByteArray(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    public static int fourFirstBytesToInt(byte[] array) {
        if (array.length < 4) {
            return -1;
        }
        byte[] temp = new byte[4];
        for (int i = 0; i < 4; i ++) {
            temp[i] = array[i];
        }
        return ByteBuffer.wrap(temp).getInt();
    }

    public static byte[] prependLengthToByteArray(byte[] byteArray) {
        return prependLengthToByteArray(byteArray, byteArray.length);
    }

    public static byte[] prependLengthToByteArray(byte[] byteArray, int length) {
        byte[] intBytes = intToByteArray(length);
        try {
            return concatByteArrays(intBytes, byteArray);
        } catch (IOException e) {
            EyesInTheShadows.error("Failed to concatenate byte arrays!");
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] removeFourLeadingBytes(byte[] byteArray) {
        return removeXLeadingBytes(4, byteArray);
    }

    public static byte[] removeXLeadingBytes(int amount, byte[] byteArray) {
        if (byteArray.length < amount) {
            return new byte[0];
        }
        byte [] res = new byte[byteArray.length - amount];
        for (int i = amount; i < byteArray.length; i ++) {
            res[i - amount] = byteArray[i];
        }
        return res;
    }

    public static byte[] getXLeadingBytes(int amount, byte[] byteArray) {
        byte[] res = new byte[amount];
        for (int i = 0; i < amount; i ++) {
            if (i < byteArray.length) {
                res[i] = byteArray[i];
            }
        }
        return res;
    }

    public static void registerSpawnEgg(String name, int primaryColor, int secondaryColor) {
        Item itemSpawnEgg = new EyesMonsterPlacer(name, primaryColor, secondaryColor).setUnlocalizedName("spawn_egg_" + name.toLowerCase()).setTextureName(Tags.MODID + ":spawn_egg");
        GameRegistry.registerItem(itemSpawnEgg, "spawnEgg" + name);
    }

    public static float getSunBrightnessBody(float angle, World w)
    {
        float f1 = w.getCelestialAngle(angle);
        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.2F);

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float)((double)f2 * (1.0D - (double)(w.getRainStrength(angle) * 5.0F) / 16.0D));
        f2 = (float)((double)f2 * (1.0D - (double)(w.getWeightedThunderStrength(angle) * 5.0F) / 16.0D));
        return f2 * 0.8F + 0.2F;
    }

    public static float getSunBrightness(World world) {
        if (world.provider.hasNoSky) {
            return 0.F;
        }
        //System.out.println("sunBrightness: " + world.provider.getSunBrightness(1.0F));
        //System.out.println("starBrightness: " + world.provider.getStarBrightness(1.0F));

        //return world.provider.getSunBrightness(1.0F);
        return getSunBrightnessBody(1.0F, world);
    }

    public static float getLightSourceBrightness(World world, int x, int y, int z) {
        return world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) / 15.F;
    }

    /* Calculating how transparent the eyes should be, from 0 to 1 */
    public static float getEyeRenderingAlpha(Entity entity, boolean ignoreArtificialLight) {
        float mixAlpha;
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY);
        int z = MathHelper.floor_double(entity.posZ);
        float artificialLight = 0;
        if (!ignoreArtificialLight) {
            artificialLight = Util.getLightSourceBrightness(entity.worldObj, x, y, z);
        }

        /* A value of 15 here means the block is exposed to the sky */
        if (entity.worldObj.getBlockLightValue(x, y, z) == 15) {
            /* If the entity is exposed to the sky, we subtract the sun brightness and the brightness from torches/other light emitting blocks */
            mixAlpha = 1 - Util.getSunBrightness(entity.worldObj) - artificialLight;
        } else {
            /* Otherwise, we subtract the light emitted by nearby blocks * 1.1, and the getBrightness * sun brightness.
             * getBrightness calculates how much the entity receives light from the sky. Then we multiply because the more day it is, the more we want to actually "enforce it".
             * It blends pretty well.
             *  */
            mixAlpha = 1 - artificialLight * 1.1F - (entity.getBrightness(1.0F) * Util.getSunBrightness(entity.worldObj));
        }

        return Math.max(mixAlpha, 0);
    }

    /* from Minecraft 1.19 net.minecraft.util.Mth */
    public static double lerp(double p_14140_, double p_14141_, double p_14142_) {
        return p_14141_ + p_14140_ * (p_14142_ - p_14141_);
    }

    /* from Minecraft 1.19 net.minecraft.util.Mth */
    public static double clampedLerp(double p_14086_, double p_14087_, double p_14088_) {
        if (p_14088_ < 0.0D) {
            return p_14086_;
        } else {
            return p_14088_ > 1.0D ? p_14087_ : lerp(p_14088_, p_14086_, p_14087_);
        }
    }

    /* from Minecraft 1.19 net.minecraft.util.Mth */
    public static float clampedLerp(float p_144921_, float p_144922_, float p_144923_) {
        if (p_144923_ < 0.0F) {
            return p_144921_;
        } else {
            return p_144923_ > 1.0F ? p_144922_ : (float) lerp(p_144923_, p_144921_, p_144922_);
        }
    }

    public static enum Color {
        DARKRED,
        RED,
        GOLD,
        YELLOW,
        DARKGREEN,
        GREEN,
        AQUA,
        DARKAQUA,
        DARKBLUE,
        BLUE,
        LIGHTPURPLE,
        DARKPURPLE,
        WHITE,
        GREY,
        DARKGREY,
        BLACK,

        RESET
    }

    public static String colorCode(Color color) {
        Map<Color, String> colorMap = new HashMap<Color, String>() {{
            put(Color.DARKRED, "4");
            put(Color.RED, "c");
            put(Color.GOLD, "6");
            put(Color.YELLOW, "e");
            put(Color.DARKGREEN, "2");
            put(Color.GREEN, "a");
            put(Color.AQUA, "b");
            put(Color.DARKAQUA, "3");
            put(Color.DARKBLUE, "1");
            put(Color.BLUE, "9");
            put(Color.LIGHTPURPLE, "d");
            put(Color.DARKPURPLE, "5");
            put(Color.WHITE, "f");
            put(Color.GREY, "7");
            put(Color.DARKGREY, "8");
            put(Color.BLACK, "0");
            put(Color.RESET, "r");

        }};
        return (char) 167 + colorMap.get(color);
    }

    public static Object[] addAtIndex(Object[] arr, int index, Object val) {
        if (index < 0 || index > arr.length) {
            return null;
        }
        Object[] res = new Object[arr.length + 1];
        for (int i = 0, j = 0; i < res.length; i ++) {
            if (i == index) {
                res[i] = val;
            } else {
                res[i] = arr[j];
                j ++;
            }
        }
        return res;
    }

    public static Object[] removeAtIndex(Object[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            return null;
        }
        Object[] res = new Object[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i ++) {
            if (i != index) {
                res[j] = arr[i];
                j ++;
            }
        }
        return res;
    }
}
