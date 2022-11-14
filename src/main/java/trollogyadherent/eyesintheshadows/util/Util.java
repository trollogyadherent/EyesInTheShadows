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

    public static boolean isSunVisible(World world, int x, int y, int z)
    {
        return (world.isDaytime()) && (!world.provider.hasNoSky) && (world.canBlockSeeTheSky(x, y, z)) && (((world.getWorldChunkManager().getBiomeGenAt(x, z) instanceof BiomeGenDesert)) || ((!world.isRaining()) && (!world.isThundering())));
    }

    public static double getAverageLightLevel(Entity entity) {
        int coreX = MathHelper.floor_double(entity.posX);
        int coreY = MathHelper.floor_double(entity.posY);
        int coreZ = MathHelper.floor_double(entity.posZ);
        int totalLight = 0;
        int totalBlocks = 0;
        for (int x = coreX - 1; x <= coreX + 1; x++) {
            for (int y = coreY - 0; y <= coreY + 1; y++) {
                for (int z = coreZ - 1; z <= coreZ + 1; z++) {
                    if (!entity.worldObj.getBlock(x, y, z).getUseNeighborBrightness()) {
                        totalLight += entity.worldObj.getBlockLightValue_do(x, y, z, true);
                        totalBlocks++;
                        //if (debugGlass)
                        //    w.func_147449_b(x, y, z, Blocks.field_150359_w);
                    }
                }
            }
        }
        if (totalBlocks == 0)
            totalBlocks = 1;
        double avgLight = Math.floor((totalLight / totalBlocks));
        return avgLight;
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
}
