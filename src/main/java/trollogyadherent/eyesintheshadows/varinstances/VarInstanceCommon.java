package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.potion.Potion;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.util.DimensionUtil;
import trollogyadherent.eyesintheshadows.util.MobUtil;
import trollogyadherent.eyesintheshadows.util.PotionUtil;
import trollogyadherent.eyesintheshadows.util.XSTR;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class VarInstanceCommon {
    /* A faster random. http://demesos.blogspot.com/2011/09/pseudo-random-number-generators.html */
    public XSTR rand = new XSTR();
    public String disappearSound = Tags.MODID + ":" + "mob.eyes.disappear";
    public String laughSound = Tags.MODID + ":" + "mob.eyes.laugh";
    public String jumpScareSound = Tags.MODID + ":" + "mob.eyes.jumpscare";
    public Field superTargetEntityField = ReflectionHelper.findField(EntityAINearestAttackableTarget.class, "field_75309_a", "targetEntity");
    public Field speedTowardsTargetField = ReflectionHelper.findField(EntityAIAttackOnCollide.class, "field_75440_e", "speedTowardsTarget");
    //public Field settingsField = ReflectionHelper.findField(DedicatedServer.class, "field_71340_o", "settings");

    public HashMap<String, Potion> potionList;
    public HashMap<String, Potion> potionCollisionList;
    public HashMap<String, Potion> potionLookList;
    public HashMap<String, DimensionUtil.SimpleDimensionObj> dimensionList;
    public ArrayList<Class> entitiesAttackingEyesList;
    public ArrayList<Class> entitiesAttackedByEyesList;
    public ArrayList<Class> entitiesFleeingEyesList;
    public ArrayList<Class> entitiesThatEyesFleeList;

    public Hashtable<Integer, Class<? extends WorldProvider>> providers;

    public Field providersField = ReflectionHelper.findField(DimensionManager.class, "providers");
    public boolean witcheryLoaded;

    public VarInstanceCommon() {
        superTargetEntityField.setAccessible(true);
        speedTowardsTargetField.setAccessible(true);
        //settingsField.setAccessible(true);
        providersField.setAccessible(true);
    }

    public void postInitHook() {
        witcheryLoaded = Loader.isModLoaded("witchery");
        try {
            providers = (Hashtable<Integer, Class<? extends WorldProvider>>) providersField.get(null);
        } catch (IllegalAccessException e) {
            ConfigMaxxing.error("Reflection failed");
            e.printStackTrace();
        }
        buildPotionList();
        buildDimensionList();
        buildMobLists();
    }

    public void buildPotionList() {
        potionList = new HashMap<>();
        for (String s : Config.potionNames) {
            Potion p = PotionUtil.getPotionByName(s);
            if (p == null) {
                EyesInTheShadows.error("Failed to get potion for name " + s);
            } else {
                potionList.put(p.getName(), p);
            }
        }
        potionCollisionList = new HashMap<>();
        for (String s : Config.potionCollisionNames) {
            Potion p = PotionUtil.getPotionByName(s);
            if (p == null) {
                EyesInTheShadows.error("Failed to get potion for name " + s);
            } else {
                potionCollisionList.put(p.getName(), p);
            }
        }
        potionLookList = new HashMap<>();
        for (String s : Config.potionLookNames) {
            Potion p = PotionUtil.getPotionByName(s);
            if (p == null) {
                EyesInTheShadows.error("Failed to get potion for name " + s);
            } else {
                potionLookList.put(p.getName(), p);
            }
        }
    }

    public void buildBiomeList() {
        dimensionList = new HashMap<>();
        for (String s : Config.dimensionSpawnNames) {
            DimensionUtil.SimpleDimensionObj sdo = DimensionUtil.getSimpleDimensionObj(s);
            if (sdo == null) {
                EyesInTheShadows.error("Failed to get dimension for name " + s);
            } else {
                dimensionList.put(sdo.getName(), sdo);
            }
        }
    }

    public void buildDimensionList() {
        dimensionList = new HashMap<>();
        for (String s : Config.dimensionSpawnNames) {
            DimensionUtil.SimpleDimensionObj sdo = DimensionUtil.getSimpleDimensionObj(s);
            if (sdo == null) {
                EyesInTheShadows.error("Failed to get dimension for name " + s);
            } else {
                dimensionList.put(sdo.getName(), sdo);
            }
        }
    }

    public void buildMobLists() {
        entitiesAttackingEyesList = new ArrayList<>();
        for (String s : Config.mobStringsAttackingEyes) {
            String class_ = MobUtil.getClassByName(s);
            if (class_ == null) {
                EyesInTheShadows.error("Failed to get mob class for name " + s);
            } else {
                try {
                    Class c = Class.forName(class_);
                    entitiesAttackingEyesList.add(c);
                } catch (ClassNotFoundException e) {
                    EyesInTheShadows.error("Failed to get class for classname " + class_);
                }
            }
        }

        entitiesAttackedByEyesList = new ArrayList<>();
        for (String s : Config.mobStringsThatEyesAttack) {
            String class_ = MobUtil.getClassByName(s);
            if (class_ == null) {
                EyesInTheShadows.error("Failed to get mob class for name " + s);
            } else {
                try {
                    Class c = Class.forName(class_);
                    entitiesAttackedByEyesList.add(c);
                } catch (ClassNotFoundException e) {
                    EyesInTheShadows.error("Failed to get class for classname " + class_);
                }
            }
        }

        entitiesFleeingEyesList = new ArrayList<>();
        for (String s : Config.mobStringsFleeingEyes) {
            String class_ = MobUtil.getClassByName(s);
            if (class_ == null) {
                EyesInTheShadows.error("Failed to get mob class for name " + s);
            } else {
                try {
                    Class c = Class.forName(class_);
                    entitiesFleeingEyesList.add(c);
                } catch (ClassNotFoundException e) {
                    EyesInTheShadows.error("Failed to get class for classname " + class_);
                }
            }
        }

        entitiesThatEyesFleeList = new ArrayList<>();
        for (String s : Config.mobStringsThatEyesFlee) {
            String class_ = MobUtil.getClassByName(s);
            if (class_ == null) {
                EyesInTheShadows.error("Failed to get mob class for name " + s);
            } else {
                try {
                    Class c = Class.forName(class_);
                    entitiesThatEyesFleeList.add(c);
                } catch (ClassNotFoundException e) {
                    EyesInTheShadows.error("Failed to get class for classname " + class_);
                }
            }
        }
    }
}
