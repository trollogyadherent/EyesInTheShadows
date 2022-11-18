package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.ResourceLocation;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.configpickers.mob.MobRenderTicker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class VarInstanceClient {
    public ResourceLocation eyesTexture = new ResourceLocation(Tags.MODID+":textures/entity/eyes1.png");
    public ResourceLocation eyes2Texture = new ResourceLocation(Tags.MODID+":textures/entity/eyes2.png");
    public ResourceLocation creepyTexture = new ResourceLocation(Tags.MODID+":textures/creepy.png");

    public Field entryListField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "entryList");
    public Field currentValuesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "currentValues");
    public Field beforeValuesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "beforeValues");
    public Field enabledField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "enabled");
    public Field owningGuiField = ReflectionHelper.findField(GuiEditArrayEntries.class, "owningGui");
    //public Field owningGuiGuiEditArrayField = ReflectionHelper.findField(GuiEditArray.class, "owningGui");
    public Field btnDoneField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnDone");
    public Field btnDefaultField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnDefault");
    public Field btnUndoChangesField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiEditArray.class, "btnUndoChanges");
    public Field chkApplyGloballyField = ReflectionHelper.findField(cpw.mods.fml.client.config.GuiConfig.class, "chkApplyGlobally");

    public int xmod = 0;
    public int ymod = 0;
    public int zmod = 0;

    public int renderpass = 1;

    public float hmod = 0.15F;
    public float hmod2 = 0.25F;

    public MobRenderTicker mobRenderTicker;

    public VarInstanceClient() {
        entryListField.setAccessible(true);
        currentValuesField.setAccessible(true);
        beforeValuesField.setAccessible(true);
        owningGuiField.setAccessible(true);
        btnDoneField.setAccessible(true);
        btnDefaultField.setAccessible(true);
        btnUndoChangesField.setAccessible(true);
        enabledField.setAccessible(true);
        chkApplyGloballyField.setAccessible(true);
    }
}
