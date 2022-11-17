package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.client.config.GuiConfigEntries;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import trollogyadherent.eyesintheshadows.gui.ArrayEntryPotionID;
import trollogyadherent.eyesintheshadows.util.Util;

import java.io.File;

public class Config {
    public static Configuration config = new Configuration(EyesInTheShadows.confFile);
    static boolean loaded = false;
    
    private static class Defaults {
        /* general */
        public static final boolean debugMode = false;
        public static final boolean jumpscare = true;
        public static final boolean eyesCanAttackWhileLit = false;
        public static final double speedNoAggro = 0.1;
        public static final double speedFullAggro = 0.5;

        /* eye aggression */
        public static final boolean enableEyeAggressionEscalation = true;
        public static final boolean eyeAggressionDependsOnLocalDifficulty = true;
        public static final boolean eyeAggressionDependsOnLightLevel = true;
        public static final boolean changeColorWithAggro = true;

        /* spawning */
        public static final int maxSpawnedInChunk = 8;
        public static final int despawnAfterAmountOfTicks = 2400;

        /* misc */
        public static final float aggroEscalationPerTick = 1f / (20 * 60 * 5);
        public static final int blinkDuration = 5;
        public static final float blinkChance = 0.02F;
        public static final int health = 1;
        public static final float scaleFactor = 0.75F;
        public static final int tickBetweenAttacks = 40;
        public static final boolean eyesAttackTamedWolves = false;
        public static final boolean wolvesAttackEyes = true;
        public static final boolean wolvesAttackDormantEyes = false;
        public static final double eyeBaseAttackDamage = 1;
        public static final boolean eyesFleeOcelots = true;
        public static final float watchDistance = 16F;
        public static final boolean eyesWander = true;
        public static final boolean printPotions = false;
        public static final int[] potionIds = {};
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
    public static boolean eyesCanAttackWhileLit = Defaults.eyesCanAttackWhileLit;
    public static double speedNoAggro = Defaults.speedNoAggro;
    public static double speedFullAggro = Defaults.speedFullAggro;

    /* eye aggression */
    public static boolean enableEyeAggressionEscalation = Defaults.enableEyeAggressionEscalation;
    public static boolean eyeAggressionDependsOnLocalDifficulty = Defaults.eyeAggressionDependsOnLocalDifficulty;
    public static boolean eyeAggressionDependsOnLightLevel = Defaults.eyeAggressionDependsOnLightLevel;
    public static boolean changeColorWithAggro = Defaults.changeColorWithAggro;

    /* spawning */
    public static int maxSpawnedInChunk = Defaults.maxSpawnedInChunk;
    public static int despawnAfterAmountOfTicks = Defaults.despawnAfterAmountOfTicks;

    /* misc */
    public static float aggroEscalationPerTick = Defaults.aggroEscalationPerTick;
    public static int blinkDuration = Defaults.blinkDuration;
    public static float blinkChance = Defaults.blinkChance;
    public static int health = Defaults.health;
    public static float scaleFactor = Defaults.scaleFactor;
    public static int tickBetweenAttacks = Defaults.tickBetweenAttacks;
    public static boolean eyesAttackTamedWolves = Defaults.eyesAttackTamedWolves;
    public static boolean wolvesAttackEyes = Defaults.wolvesAttackEyes;
    public static boolean wolvesAttackDormantEyes = Defaults.wolvesAttackDormantEyes;
    public static double eyeBaseAttackDamage = Defaults.eyeBaseAttackDamage;
    public static boolean eyesFleeOcelots = Defaults.eyesFleeOcelots;
    public static float watchDistance = Defaults.watchDistance;
    public static boolean eyesWander = Defaults.eyesWander;
    public static boolean printPotions = Defaults.printPotions;
    public static int[] potionIds = Defaults.potionIds;

    public static void synchronizeConfigurationClient(File configFile, boolean force, boolean load) {
        if (!loaded || force) {
            if (load) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();

            /* misc */
            Property blinkChanceProp = config.get(Categories.misc, "blinkChance", Defaults.blinkChance, "How often Eyes should blink.", 0, 1);
            if (!Util.isServer()) {
                blinkChanceProp.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
            }
            blinkChance = (float) blinkChanceProp.getDouble();

            Property scaleFactorProperty = config.get(Categories.misc, "scale_factor", Defaults.scaleFactor, "Eye size scale factor (purely visual).", 0.01F, Float.MAX_VALUE);
            scaleFactor = (float) scaleFactorProperty.getDouble();

            Property potionIdsProperty = config.get(Categories.misc, "potionIds", Defaults.potionIds, "List of potion effect id's that should be applied to a player attacked by Eyes.");
            if (!Util.isServer()) {
                potionIdsProperty.setConfigEntryClass(ArrayEntryPotionID.class);
            }
            potionIds = potionIdsProperty.getIntList();
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

            Property blinkChanceProp = config.get(Categories.misc, "blinkChance", Defaults.blinkChance, "How often eyes should blink", 0, 1);
            blinkChance = (float) blinkChanceProp.getDouble();

            Property potionIdsProperty = config.get(Categories.misc, "potionIds", Defaults.potionIds, "List of potion effect id's that should be applied to a player attacked by Eyes.");
            potionIds = potionIdsProperty.getIntList();

            synchronizeConfigurationCommon();
        }
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationCommon() {
        /* general */
        Property debugModeProperty = config.get(Categories.general, "debugMode", Defaults.debugMode, "Enable/disable debug logs.");
        debugMode = debugModeProperty.getBoolean();

        Property jumpscareProperty = config.get(Categories.general, "jumpscare", Defaults.jumpscare, "Set to false to disable the jumpscare system.");
        jumpscare = jumpscareProperty.getBoolean();

        Property eyesCanAttackWhileLitProperty = config.get(Categories.general, "eyesCanAttackWhileLit", Defaults.eyesCanAttackWhileLit, "While set to true, the eyes entity will ignore the artificial light level and will jumpscare even if it's lit. Daylight will still disable it's AI.");
        eyesCanAttackWhileLit = eyesCanAttackWhileLitProperty.getBoolean();

        Property  speedNoAggroProperty = config.get(Categories.general, "speedNoAggro", Defaults.speedNoAggro, "Eyes speed at minimal aggro.", 0, Double.MAX_VALUE);
        speedNoAggro = speedNoAggroProperty.getDouble();

        Property  speedFullAggroProperty = config.get(Categories.general, "speedFullAggro", Defaults.speedFullAggro, "Eyes speed at full aggro.", 0, Double.MAX_VALUE);
        speedFullAggro = speedFullAggroProperty.getDouble();

        /* eye aggro */
        Property enableEyeAggressionEscalationProperty = config.get(Categories.eye_aggression, "enableEyeAggressionEscalation", Defaults.enableEyeAggressionEscalation, "While set to true, the eyes entities will progressively get more bold, and move faster, the longer they live.");
        enableEyeAggressionEscalation = enableEyeAggressionEscalationProperty.getBoolean();

        Property eyeAggressionDependsOnLocalDifficultyProperty = config.get(Categories.eye_aggression, "eyeAggressionDependsOnLocalDifficulty", Defaults.eyeAggressionDependsOnLocalDifficulty, "While set to true, the eyes entities will spawn with higher aggression levels in higher local difficulties.");
        eyeAggressionDependsOnLocalDifficulty = eyeAggressionDependsOnLocalDifficultyProperty.getBoolean();

        Property eyeAggressionDependsOnLightLevelProperty = config.get(Categories.eye_aggression, "eyeAggressionDependsOnLightLevel", Defaults.eyeAggressionDependsOnLightLevel, "While set to true, the eyes entities will have higher aggression values on lower light levels.");
        eyeAggressionDependsOnLightLevel = eyeAggressionDependsOnLightLevelProperty.getBoolean();

        Property changeColorWithAggroProperty = config.get(Categories.eye_aggression, "changeColorWithAggro", Defaults.changeColorWithAggro, "While set to true, the eyes entities will become more and more red as their aggression level grows.");
        changeColorWithAggro = changeColorWithAggroProperty.getBoolean();

        /* spawning */
        Property maxSpawnedInChunkProperty = config.get(Categories.spawning, "maxSpawnedInChunk", Defaults.maxSpawnedInChunk, "Max Eyes spawning per chunk.", 1, Integer.MAX_VALUE);
        maxSpawnedInChunk = maxSpawnedInChunkProperty.getInt();

        Property despawnAfterAmountOfTicksProperty = config.get(Categories.spawning, "despawnAfterAmountOfTicks", Defaults.despawnAfterAmountOfTicks, "Ticks after which Eyes can despawn.", 1, Integer.MAX_VALUE);
        despawnAfterAmountOfTicks = despawnAfterAmountOfTicksProperty.getInt();

        /* misc */
        Property blinkDurationProperty = config.get(Categories.misc, "blinkDuration", Defaults.blinkDuration, "Eye blink duration. Set to -1 to disable blinking", -1, Integer.MAX_VALUE);
        blinkDuration = blinkDurationProperty.getInt();

        Property aggroEscalationPerTickProperty = config.get(Categories.misc, "aggroEscalationPerTick", Defaults.aggroEscalationPerTick, "Eyes aggro escalation per tick.", 0, Float.MAX_VALUE);
        aggroEscalationPerTick = (float) aggroEscalationPerTickProperty.getDouble();

        Property healthProperty = config.get(Categories.misc, "health", Defaults.health, "Eye health", 1, Integer.MAX_VALUE);
        health = healthProperty.getInt();

        Property tickBetweenAttacksProperty = config.get(Categories.misc, "tickBetweenAttacks", Defaults.tickBetweenAttacks, "Ticks between each eye attack on non player mobs.", 1, Integer.MAX_VALUE);
        tickBetweenAttacks = tickBetweenAttacksProperty.getInt();

        Property eyesAttackTamedWolvesProperty = config.get(Categories.misc, "eyesAttackTamedWolves", Defaults.eyesAttackTamedWolves, "Controls whether Eyes attack tamed Wolves.");
        eyesAttackTamedWolves = eyesAttackTamedWolvesProperty.getBoolean();

        Property wolvesAttackEyesProperty = config.get(Categories.misc, "wolvesAttackEyes", Defaults.wolvesAttackEyes, "Controls whether tamed wolves attack Eyes.");
        wolvesAttackEyes = wolvesAttackEyesProperty.getBoolean();

        Property wolvesAttackDormantEyesProperty = config.get(Categories.misc, "wolvesAttackDormantEyes", Defaults.wolvesAttackDormantEyes, "Controls whether tamed wolves attack dormant Eyes (wolvesAttackEyes must be true for the attack to happen).");
        wolvesAttackDormantEyes = wolvesAttackDormantEyesProperty.getBoolean();

        Property eyeBaseAttackDamageProperty = config.get(Categories.misc, "eyeBaseAttackDamage", Defaults.eyeBaseAttackDamage, "Base damage dealt by Eyes.", 0, Double.MAX_VALUE);
        eyeBaseAttackDamage = eyeBaseAttackDamageProperty.getDouble();

        Property eyesFleeOcelotsProperty = config.get(Categories.misc, "eyesFleeOcelots", Defaults.eyesFleeOcelots, "Controls whether Eyes flee Ocelots.");
        eyesFleeOcelots = eyesFleeOcelotsProperty.getBoolean();

        Property watchDistanceProperty = config.get(Categories.misc, "watchDistance", Defaults.watchDistance, "Distance at which Eyes watch the player.", 0, Float.MAX_VALUE);
        watchDistance = (float) watchDistanceProperty.getDouble();

        Property eyesWanderProperty = config.get(Categories.misc, "eyesWander", Defaults.eyesWander, "Controls whether Eyes wander around.");
        eyesWander = eyesWanderProperty.getBoolean();

        Property printPotionsProperty = config.get(Categories.misc, "printPotions", Defaults.printPotions, "If set to true, print a list of potions and their ID's in the logs.");
        printPotions = printPotionsProperty.getBoolean();
    }
}