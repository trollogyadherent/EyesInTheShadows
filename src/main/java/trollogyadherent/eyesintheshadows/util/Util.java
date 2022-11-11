package trollogyadherent.eyesintheshadows.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import de.matthiasmann.twl.utils.PNGDecoder;
import net.minecraft.item.Item;
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

    /* Taken from MC 1.19 */
    public static int floor(float f) {
        int i = (int)f;
        return f < (float)i ? i - 1 : i;
    }

    /* Taken from MC 1.19 */
    public static float clamp(float p_14037_, float p_14038_, float p_14039_) {
        if (p_14037_ < p_14038_) {
            return p_14038_;
        } else {
            return p_14037_ > p_14039_ ? p_14039_ : p_14037_;
        }
    }
}
