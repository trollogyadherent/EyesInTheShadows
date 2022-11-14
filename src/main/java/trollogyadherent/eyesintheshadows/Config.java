package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.client.config.GuiConfigEntries;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import trollogyadherent.eyesintheshadows.util.Util;

import java.io.File;

public class Config {
    public static Configuration config = new Configuration(EyesInTheShadows.confFile);
    static boolean loaded = false;
    
    private static class Defaults {
        /* general */
        public static final boolean debugMode = false;
        public static final boolean jumpscare = true;



        /* misc */
        public static final float aggroEscalationPerTick = 1f / (20 * 60 * 5);
        public static final int blinkDuration = 5;
        public static final float blinkChance = 0.02F;
        public static final int health = 1;
        public static final float scaleFactor = 0.75F;
    }

    public static class Categories {
        public static final String general = "general";
        public static final String eye_aggression = "eye_aggression";
        public static final String sound_volumes = "sound_volumes";
        public static final String spawning = "spawning";
        public static final String spawning_normal = "spawning_normal";
        public static final String spawning_midnight = "spawning_midnight";
        public static final String spawning_halloween = "spawning_halloween";
        public static final String misc = "misc";
    }

    /* general */
    public static boolean debugMode = Defaults.debugMode;
    public static boolean jumpscare = Defaults.jumpscare;


    /* misc */
    public static float aggroEscalationPerTick = Defaults.aggroEscalationPerTick;
    public static int blinkDuration = Defaults.blinkDuration;
    public static float blinkChance = Defaults.blinkChance;
    public static int health = Defaults.health;
    public static float scaleFactor = Defaults.scaleFactor;


    public static void synchronizeConfigurationClient(File configFile, boolean force, boolean load) {
        if (!loaded || force) {
            if (load) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();

            /* misc */
            Property blinkDurationProperty = config.get(Categories.misc, "blinkDuration", Defaults.blinkDuration, "Eye blink duration. Set to -1 to disable blinking", -1, Integer.MAX_VALUE);
            blinkDuration = blinkDurationProperty.getInt();

            Property blinkChanceProp = config.get(Categories.misc, "blinkChance", Defaults.blinkChance, "How often eyes should blink", 0, 1);
            if (!Util.isServer()) {
                blinkChanceProp.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
            }
            blinkChance = (float) blinkChanceProp.getDouble();
        }
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationServer(File configFile, boolean force) {
        if (!loaded || force) {
            if (loaded) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();
        }
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationCommon() {
        /* general */
        Property debugModeProperty = config.get(Categories.general, "debugMode", Defaults.debugMode, "Enable/disable debug logs");
        debugMode = debugModeProperty.getBoolean();

        Property jumpscareProperty = config.get(Categories.general, "jumpscare", Defaults.jumpscare, "Set to false to disable the jumpscare system.");
        jumpscare = jumpscareProperty.getBoolean();


        /* misc */
        Property aggroEscalationPerTickProperty = config.get(Categories.misc, "aggroEscalationPerTick", Defaults.aggroEscalationPerTick, "Eyes aggro escalation per tick", 0, Float.MAX_VALUE);
        aggroEscalationPerTick = (float) aggroEscalationPerTickProperty.getDouble();

        Property healthProperty = config.get(Categories.misc, "health", Defaults.health, "Eye health", 1, Integer.MAX_VALUE);
        health = healthProperty.getInt();

        Property scaleFactorProperty = config.get(Categories.misc, "scale_factor", Defaults.scaleFactor, "Eye size scale factor", 0.01F, Float.MAX_VALUE);
        scaleFactor = (float) scaleFactorProperty.getDouble();
    }
}