package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.ResourceLocation;
import trollogyadherent.eyesintheshadows.Tags;

import java.lang.reflect.Method;

public class VarInstanceClient {
    public ResourceLocation eyesTexture = new ResourceLocation(Tags.MODID+":textures/entity/eyes1.png");
    public ResourceLocation eyes2Texture = new ResourceLocation(Tags.MODID+":textures/entity/eyes2.png");
    public ResourceLocation creepyTexture = new ResourceLocation(Tags.MODID+":textures/creepy.png");

    public String disappearSound = Tags.MODID + ":" + "mob.eyes.disappear";
    public String laughSound = Tags.MODID + ":" + "mob.eyes.laugh";
    public String jumpScareSound = Tags.MODID + ":" + "mob.eyes.jumpscare";

    //public Method computeLightValueMethod = ReflectionHelper.findMethod(net.minecraft.world.World.class, "")

    public int xmod = 0;
    public int ymod = 0;
    public int zmod = 0;

    public int renderpass = 1;

    public float hmod = 0.15F;
    public float hmod2 = 0.25F;
}
