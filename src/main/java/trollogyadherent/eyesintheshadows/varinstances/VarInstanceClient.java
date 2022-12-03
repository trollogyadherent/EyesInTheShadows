package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.common.Loader;
import net.minecraft.util.ResourceLocation;
import trollogyadherent.eyesintheshadows.Tags;

public class VarInstanceClient {
    public ResourceLocation eyesTexture = new ResourceLocation(Tags.MODID+":textures/entity/eyes1.png");
    public ResourceLocation eyes2Texture = new ResourceLocation(Tags.MODID+":textures/entity/eyes2.png");
    public ResourceLocation creepyTexture = new ResourceLocation(Tags.MODID+":textures/creepy.png");

    public boolean configMaxxingLoaded;

    public int xmod = 0;
    public int ymod = 0;
    public int zmod = 0;

    public int renderpass = 1;

    public float hmod = 0.15F;
    public float hmod2 = 0.25F;
    public VarInstanceClient() {

    }

    public void postInitHook() {

    }

    public void preInitHook() {
        configMaxxingLoaded = Loader.isModLoaded("configmaxxing");
    }
}
