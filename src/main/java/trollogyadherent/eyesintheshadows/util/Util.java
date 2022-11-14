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
import net.minecraft.world.biome.BiomeGenDesert;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.spawnegg.EyesMonsterPlacer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

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

    public static float getSunBrightness(World world) {
        if (world.provider.hasNoSky) {
            return 0.F;
        }
        //System.out.println("sunBrightness: " + world.provider.getSunBrightness(1.0F));
        //System.out.println("starBrightness: " + world.provider.getStarBrightness(1.0F));
        return world.provider.getSunBrightness(1.0F);
    }

    public static float getLightSourceBrightness(World world, int x, int y, int z) {
        return world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) / 15.F;
    }

    /* Calculating how transparent the eyes should be, from 0 to 1 */
    public static float getEyeRenderingAlpha(Entity entity) {
        float mixAlpha = 1;
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY);
        int z = MathHelper.floor_double(entity.posZ);

        /* A value of 15 here means the block is exposed to the sky */
        if (entity.worldObj.getBlockLightValue(x, y, z) == 15) {
            /* If the entity is exposed to the sky, we subtract the sun brightness and the brightness from torches/other light emitting blocks */
            mixAlpha = 1 - Util.getSunBrightness(entity.worldObj) - Util.getLightSourceBrightness(entity.worldObj, x, y, z);
        } else {
            /* Otherwise, we subtract the light emitted by nearby blocks * 1.1, and the getBrightness * sun brightness.
             * getBrightness calculates how much the entity receives light from the sky. Then we multiply because the more day it is, the more we want to actually "enforce it".
             * It blends pretty well.
             *  */
            mixAlpha = 1 - Util.getLightSourceBrightness(entity.worldObj, x, y, z) * 1.1F - (entity.getBrightness(1.0F) * Util.getSunBrightness(entity.worldObj));
        }

        return Math.max(mixAlpha, 0);
    }
}
