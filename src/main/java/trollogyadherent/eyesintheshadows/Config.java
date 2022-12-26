package trollogyadherent.eyesintheshadows;

import cpw.mods.fml.client.config.GuiConfigEntries;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import trollogyadherent.configmaxxing.configpickers.biome.BiomeEntryPoint;
import trollogyadherent.configmaxxing.configpickers.dimension.DimensionEntryPoint;
import trollogyadherent.configmaxxing.configpickers.mob.MobEntryPoint;
import trollogyadherent.configmaxxing.configpickers.potion.PotionEntryPoint;
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

        /* potion */
        public static final String[] potionNames = {};
        public static final int potionDuration = 10;
        public static final int potionAmplifier = 0;
        public static final String[] potionCollisionNames = {};
        public static final int potionCollisionDuration = 10;
        public static final int potionCollisionAmplifier = 0;
        public static final String[] potionLookNames = {};
        public static final int potionLookDuration = 10;
        public static final int potionLookAmplifier = 0;

        /* eye aggression */
        public static final boolean enableEyeAggressionEscalation = true;
        public static final boolean eyeAggressionDependsOnLocalDifficulty = true;
        public static final boolean eyeAggressionDependsOnLightLevel = true;
        public static final boolean changeColorWithAggro = true;
        public static final double eyeBaseAttackDamage = 1;
        public static final boolean fly = false;

        /* spawning */
        public static final int maxSpawnedInChunk = 8;
        public static final int despawnAfterAmountOfTicks = 2400;
        public static final boolean enableNaturalSpawn = true;
        public static final String[] biomeSpawnNames = new String[0];
        public static final boolean biomeListIsWhitelist = true;
        public static final String[] dimensionSpawnNames = new String[0];
        public static final boolean dimensionListIsWhitelist = true;

        public static final int spawnCycleIntervalNormal = 150;
        public static final int maxEyesAroundPlayerNormal = 2;
        public static final int maxTotalEyesPerDimensionNormal = 15;

        public static final int spawnCycleIntervalMidnight = 50;
        public static final int maxEyesAroundPlayerMidnight = 3;
        public static final int maxTotalEyesPerDimensionMidnight = 15;

        public static final int spawnCycleIntervalHalloween = 50;
        public static final int maxEyesAroundPlayerHalloween = 5;
        public static final int maxTotalEyesPerDimensionHalloween = 25;
        public static final float maxEyesSpawnDistance = 64;

        /* misc */
        public static final float aggroEscalationPerTick = 1f / (20 * 60 * 5);
        public static final int blinkDuration = 5;
        public static final float blinkChance = 0.02F;
        public static final int health = 1;
        public static final float scaleFactor = 0.75F;
        public static final int tickBetweenAttacks = 40;
        public static final boolean eyesAttackTamedWolves = false;
        public static final boolean wolvesAttackEyes = true;
        public static final boolean mobsAttackDormantEyes = false;
        public static final float watchDistance = 16F;
        public static final boolean eyesWander = true;
        public static final boolean printPotions = false;
        public static final boolean printMobs = false;
        public static final String[] mobStringsAttackingEyes = {};
        public static final String[] mobStringsThatEyesAttack = {};
        public static final String[] mobStringsFleeingEyes = {};
        public static final String[] mobStringsThatEyesFlee = {};
        public static final boolean mobsFleeDormantEyes = false;
        public static final float damageFromWet = 0;
        public static final int spawnCycleSpawnWarningTime = 200;

        /* sound_volumes */
        public static final float eyeIdleVolume = 1;
        public static final float eyeDisappearVolume = 1;
        public static final float eyeJumpscareVolume = 1;
    }

    public static class Categories {
        public static final String general = "general";
        public static final String eye_aggression = "eye_aggression";
        public static final String sound_volumes = "sound_volumes";
        public static final String spawning = "spawning";
        public static final String misc = "misc";
        public static final String potion = "potion";
        public static final String mob_interactions = "mob_interactions";
        public static final String visual = "visual";
    }

    /* general */
    public static boolean debugMode = Defaults.debugMode;
    public static boolean jumpscare = Defaults.jumpscare;
    public static boolean eyesCanAttackWhileLit = Defaults.eyesCanAttackWhileLit;

    /* potion */
    public static String[] potionNames = Defaults.potionNames;
    public static int potionDuration = Defaults.potionDuration;
    public static int potionAmplifier = Defaults.potionAmplifier;
    public static String[] potionCollisionNames = Defaults.potionCollisionNames;
    public static int potionCollisionDuration = Defaults.potionCollisionDuration;
    public static int potionCollisionAmplifier = Defaults.potionCollisionAmplifier;
    public static String[] potionLookNames = Defaults.potionLookNames;
    public static int potionLookDuration = Defaults.potionLookDuration;
    public static int potionLookAmplifier = Defaults.potionLookAmplifier;

    /* eye aggression */
    public static boolean enableEyeAggressionEscalation = Defaults.enableEyeAggressionEscalation;
    public static boolean eyeAggressionDependsOnLocalDifficulty = Defaults.eyeAggressionDependsOnLocalDifficulty;
    public static boolean eyeAggressionDependsOnLightLevel = Defaults.eyeAggressionDependsOnLightLevel;
    public static boolean changeColorWithAggro = Defaults.changeColorWithAggro;
    public static double speedNoAggro = Defaults.speedNoAggro;
    public static double speedFullAggro = Defaults.speedFullAggro;
    public static float aggroEscalationPerTick = Defaults.aggroEscalationPerTick;
    public static int tickBetweenAttacks = Defaults.tickBetweenAttacks;
    public static float watchDistance = Defaults.watchDistance;
    public static double eyeBaseAttackDamage = Defaults.eyeBaseAttackDamage;
    public static boolean fly = true;

    /* spawning */
    public static int maxSpawnedInChunk = Defaults.maxSpawnedInChunk;
    public static float maxSpawnDistance;
    public static int despawnAfterAmountOfTicks = Defaults.despawnAfterAmountOfTicks;
    public static boolean enableNaturalSpawn = Defaults.enableNaturalSpawn;
    public static String[] biomeSpawnNames = Defaults.biomeSpawnNames;
    public static boolean biomeListIsWhitelist = Defaults.biomeListIsWhitelist;
    public static String[] dimensionSpawnNames = Defaults.dimensionSpawnNames;
    public static boolean dimensionListIsWhitelist = Defaults.dimensionListIsWhitelist;
    public static int spawnCycleIntervalNormal = Defaults.spawnCycleIntervalNormal;
    public static int maxEyesAroundPlayerNormal = Defaults.maxEyesAroundPlayerNormal;
    public static int maxTotalEyesPerDimensionNormal = Defaults.maxTotalEyesPerDimensionNormal;
    public static int spawnCycleIntervalMidnight = Defaults.spawnCycleIntervalMidnight;
    public static int maxEyesAroundPlayerMidnight = Defaults.maxEyesAroundPlayerMidnight;
    public static int maxTotalEyesPerDimensionMidnight = Defaults.maxTotalEyesPerDimensionMidnight;
    public static int spawnCycleIntervalHalloween = Defaults.spawnCycleIntervalHalloween;
    public static int maxEyesAroundPlayerHalloween = Defaults.maxEyesAroundPlayerHalloween;
    public static int maxTotalEyesPerDimensionHalloween = Defaults.maxTotalEyesPerDimensionHalloween;

    public static float maxEyesSpawnDistance = 64;

    /* sounds */
    public static float eyeIdleVolume = Defaults.eyeIdleVolume;
    public static float eyeDisappearVolume = Defaults.eyeDisappearVolume;
    public static float eyeJumpscareVolume = Defaults.eyeJumpscareVolume;

    /* misc */
    public static int blinkDuration = Defaults.blinkDuration;
    public static float blinkChance = Defaults.blinkChance;
    public static int health = Defaults.health;
    public static float scaleFactor = Defaults.scaleFactor;
    public static boolean eyesAttackTamedWolves = Defaults.eyesAttackTamedWolves;
    public static boolean wolvesAttackEyes = Defaults.wolvesAttackEyes;
    public static boolean mobssAttackDormantEyes = Defaults.mobsAttackDormantEyes;
    public static boolean eyesWander = Defaults.eyesWander;
    public static boolean printPotions = Defaults.printPotions;
    public static boolean printMobs = Defaults.printMobs;
    public static String[] mobStringsAttackingEyes = Defaults.mobStringsAttackingEyes;
    public static String[] mobStringsThatEyesAttack = Defaults.mobStringsThatEyesAttack;
    public static String[] mobStringsFleeingEyes = Defaults.mobStringsFleeingEyes;
    public static String[] mobStringsThatEyesFlee = Defaults.mobStringsThatEyesFlee;
    public static boolean mobsFleeDormantEyes = Defaults.mobsFleeDormantEyes;
    public static float damageFromWet = Defaults.damageFromWet;
    public static int spawnCycleSpawnWarningTime = Defaults.spawnCycleSpawnWarningTime;

    public static void synchronizeConfigurationClient(File configFile, boolean force, boolean load) {
        if (!loaded || force) {
            if (load) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();


            /* Visual */
            Property scaleFactorProperty = config.get(Categories.visual, "scale_factor", Defaults.scaleFactor, "Eye size scale factor (purely visual).", 0.01F, Float.MAX_VALUE);
            scaleFactor = (float) scaleFactorProperty.getDouble();

            /* Sound */
            Property eyeIdleVolumeProperty = config.get(Categories.sound_volumes, "eyeIdleVolume", Defaults.eyeIdleVolume, "Eye idle sound volume.", 0, Float.MAX_VALUE);
            eyeIdleVolume = (float) eyeIdleVolumeProperty.getDouble();

            Property eyeDisappearVolumeProperty = config.get(Categories.sound_volumes, "eyeDisappearVolume", Defaults.eyeDisappearVolume, "Eye disappearing sound volume.", 0, Float.MAX_VALUE);
            eyeDisappearVolume = (float) eyeDisappearVolumeProperty.getDouble();

            Property eyeJumpscareVolumeProperty = config.get(Categories.sound_volumes, "eyeJumpscareVolume", Defaults.eyeJumpscareVolume, "Jumpscare sound volume.", 0, Float.MAX_VALUE);
            eyeJumpscareVolume = (float) eyeJumpscareVolumeProperty.getDouble();
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
        Property debugModeProperty = config.get(Categories.general, "debugMode", Defaults.debugMode, "Enable/disable debug logs.");
        debugMode = debugModeProperty.getBoolean();

        Property jumpscareProperty = config.get(Categories.general, "jumpscare", Defaults.jumpscare, "Set to false to disable the jumpscare system.");
        jumpscare = jumpscareProperty.getBoolean();

        Property eyesCanAttackWhileLitProperty = config.get(Categories.general, "eyesCanAttackWhileLit", Defaults.eyesCanAttackWhileLit, "While set to true, the eyes entity will ignore the artificial light level and will jumpscare even if it's lit. Daylight will still disable it's AI.");
        eyesCanAttackWhileLit = eyesCanAttackWhileLitProperty.getBoolean();

        Property healthProperty = config.get(Categories.general, "health", Defaults.health, "Eye health", 1, Integer.MAX_VALUE);
        health = healthProperty.getInt();

        Property eyesWanderProperty = config.get(Categories.general, "eyesWander", Defaults.eyesWander, "Controls whether Eyes wander around.");
        eyesWander = eyesWanderProperty.getBoolean();

        /* potion */
        Property potionNamesProperty = config.get(Categories.potion, "potionNames", Defaults.potionNames, "List of potion effect names that should be applied to a player attacked by Eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            potionNamesProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionNames = potionNamesProperty.getStringList();

        Property potionDurationProperty = config.get(Categories.potion, "potionDuration", Defaults.potionDuration, "Attack inflicted potion effect duration.", 0, Integer.MAX_VALUE);
        potionDuration = potionDurationProperty.getInt();

        Property potionAmplifierProperty = config.get(Categories.potion, "potionAmplifier", Defaults.potionAmplifier, "Attack inflicted potion effect amplifier.", 0, Integer.MAX_VALUE);
        potionAmplifier = potionAmplifierProperty.getInt();

        Property potionCollisionNamesProperty = config.get(Categories.potion, "potionCollisionNames", Defaults.potionCollisionNames, "List of potion effect names that should be applied to a player colliding with the eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            potionCollisionNamesProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionCollisionNames = potionCollisionNamesProperty.getStringList();

        Property potionCollisionDurationProperty = config.get(Categories.potion, "potionCollisionDuration", Defaults.potionCollisionDuration, "Collision inflicted potion effect duration.", 0, Integer.MAX_VALUE);
        potionCollisionDuration = potionCollisionDurationProperty.getInt();

        Property potionCollisionAmplifierProperty = config.get(Categories.potion, "potionCollisionAmplifier", Defaults.potionCollisionAmplifier, "Collision inflicted potion effect amplifier.", 0, Integer.MAX_VALUE);
        potionCollisionAmplifier = potionCollisionAmplifierProperty.getInt();

        Property potionLookNamesProperty = config.get(Categories.potion, "potionLookNames", Defaults.potionLookNames, "List of potion effect names that should be applied to a player looking at the eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            potionLookNamesProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionLookNames = potionLookNamesProperty.getStringList();

        Property potionLookDurationProperty = config.get(Categories.potion, "potionLookDuration", Defaults.potionLookDuration, "Look inflicted potion effect duration.", 0, Integer.MAX_VALUE);
        potionLookDuration = potionLookDurationProperty.getInt();

        Property potionLookAmplifierProperty = config.get(Categories.potion, "potionLookAmplifier", Defaults.potionLookAmplifier, "Look inflicted potion effect amplifier.", 0, Integer.MAX_VALUE);
        potionLookAmplifier = potionLookAmplifierProperty.getInt();


        /* eye aggro */
        Property eyeBaseAttackDamageProperty = config.get(Categories.eye_aggression, "eyeBaseAttackDamage", Defaults.eyeBaseAttackDamage, "Base damage dealt by Eyes.", 0, Double.MAX_VALUE);
        eyeBaseAttackDamage = eyeBaseAttackDamageProperty.getDouble();

        Property enableEyeAggressionEscalationProperty = config.get(Categories.eye_aggression, "enableEyeAggressionEscalation", Defaults.enableEyeAggressionEscalation, "While set to true, the eyes entities will progressively get more bold, and move faster, the longer they live.");
        enableEyeAggressionEscalation = enableEyeAggressionEscalationProperty.getBoolean();

        Property eyeAggressionDependsOnLocalDifficultyProperty = config.get(Categories.eye_aggression, "eyeAggressionDependsOnLocalDifficulty", Defaults.eyeAggressionDependsOnLocalDifficulty, "While set to true, the eyes entities will spawn with higher aggression levels in higher local difficulties.");
        eyeAggressionDependsOnLocalDifficulty = eyeAggressionDependsOnLocalDifficultyProperty.getBoolean();

        Property eyeAggressionDependsOnLightLevelProperty = config.get(Categories.eye_aggression, "eyeAggressionDependsOnLightLevel", Defaults.eyeAggressionDependsOnLightLevel, "While set to true, the eyes entities will have higher aggression values on lower light levels.");
        eyeAggressionDependsOnLightLevel = eyeAggressionDependsOnLightLevelProperty.getBoolean();

        Property changeColorWithAggroProperty = config.get(Categories.eye_aggression, "changeColorWithAggro", Defaults.changeColorWithAggro, "While set to true, the eyes entities will become more and more red as their aggression level grows.");
        changeColorWithAggro = changeColorWithAggroProperty.getBoolean();

        Property  speedNoAggroProperty = config.get(Categories.eye_aggression, "speedNoAggro", Defaults.speedNoAggro, "Eyes speed at minimal aggro.", 0, Double.MAX_VALUE);
        speedNoAggro = speedNoAggroProperty.getDouble();

        Property  speedFullAggroProperty = config.get(Categories.eye_aggression, "speedFullAggro", Defaults.speedFullAggro, "Eyes speed at full aggro.", 0, Double.MAX_VALUE);
        speedFullAggro = speedFullAggroProperty.getDouble();

        Property aggroEscalationPerTickProperty = config.get(Categories.eye_aggression, "aggroEscalationPerTick", Defaults.aggroEscalationPerTick, "Eyes aggro escalation per tick.", 0, Float.MAX_VALUE);
        aggroEscalationPerTick = (float) aggroEscalationPerTickProperty.getDouble();

        Property tickBetweenAttacksProperty = config.get(Categories.eye_aggression, "tickBetweenAttacks", Defaults.tickBetweenAttacks, "Ticks between each eye attack on non player mobs.", 1, Integer.MAX_VALUE);
        tickBetweenAttacks = tickBetweenAttacksProperty.getInt();

        Property watchDistanceProperty = config.get(Categories.eye_aggression, "watchDistance", Defaults.watchDistance, "Distance at which Eyes watch the player.", 0, Float.MAX_VALUE);
        watchDistance = (float) watchDistanceProperty.getDouble();

        Property flyProperty = config.get(Categories.eye_aggression, "fly", Defaults.fly, "While set to true, the eyes will behave more like blazes, flying upwards to targets. This can be buggy.");
        fly = flyProperty.getBoolean();

        /* spawning */
        Property maxSpawnedInChunkProperty = config.get(Categories.spawning, "maxSpawnedInChunk", Defaults.maxSpawnedInChunk, "Max Eyes spawning per chunk.", 1, Integer.MAX_VALUE);
        maxSpawnedInChunk = maxSpawnedInChunkProperty.getInt();

        Property despawnAfterAmountOfTicksProperty = config.get(Categories.spawning, "despawnAfterAmountOfTicks", Defaults.despawnAfterAmountOfTicks, "Ticks after which Eyes can despawn.", 1, Integer.MAX_VALUE);
        despawnAfterAmountOfTicks = despawnAfterAmountOfTicksProperty.getInt();

        Property enableNaturalSpawnProperty = config.get(Categories.spawning, "enableNaturalSpawn", Defaults.enableNaturalSpawn, "Enable or disable natural Eye spawning.");
        enableNaturalSpawn = enableNaturalSpawnProperty.getBoolean();

        Property biomeSpawnNamesProperty = config.get(Categories.spawning, "biomeSpawnNames", Defaults.biomeSpawnNames, "List of biomes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            biomeSpawnNamesProperty.setConfigEntryClass(BiomeEntryPoint.class);
        }
        biomeSpawnNames = biomeSpawnNamesProperty.getStringList();

        Property biomeListIsWhitelistProperty = config.get(Categories.spawning, "biomeListIsWhitelist", Defaults.biomeListIsWhitelist, "Whether the biome list acts as a white or blacklist.");
        biomeListIsWhitelist = biomeListIsWhitelistProperty.getBoolean();

        Property dimensionSpawnNamesProperty = config.get(Categories.spawning, "dimensionSpawnNames", Defaults.dimensionSpawnNames, "List of dimensions.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            dimensionSpawnNamesProperty.setConfigEntryClass(DimensionEntryPoint.class);
        }
        dimensionSpawnNames = dimensionSpawnNamesProperty.getStringList();

        Property dimensionListIsWhitelistProperty = config.get(Categories.spawning, "dimensionListIsWhitelist", Defaults.dimensionListIsWhitelist, "Whether the dimension list acts as a white or blacklist.");
        dimensionListIsWhitelist = dimensionListIsWhitelistProperty.getBoolean();

        Property spawnCycleIntervalNormalProperty = config.get(Categories.spawning, "spawnCycleIntervalNormal", Defaults.spawnCycleIntervalNormal, "Number of ticks between spawn cycles.", 1, Integer.MAX_VALUE);
        spawnCycleIntervalNormal = spawnCycleIntervalNormalProperty.getInt();

        Property maxEyesAroundPlayerNormalProperty = config.get(Categories.spawning, "maxEyesAroundPlayerNormal", Defaults.maxEyesAroundPlayerNormal, "Max amount of eyes that will spawn around one player.", 1, Integer.MAX_VALUE);
        maxEyesAroundPlayerNormal = maxEyesAroundPlayerNormalProperty.getInt();

        Property maxTotalEyesPerDimensionNormalProperty = config.get(Categories.spawning, "maxTotalEyesPerDimensionNormal", Defaults.maxTotalEyesPerDimensionNormal, "Max number of eyes entities that will spawn in each dimension.", 1, Integer.MAX_VALUE);
        maxTotalEyesPerDimensionNormal = maxTotalEyesPerDimensionNormalProperty.getInt();

        Property spawnCycleIntervalMidnightProperty = config.get(Categories.spawning, "spawnCycleIntervalMidnight", Defaults.spawnCycleIntervalMidnight, "Number of ticks between spawn cycles.", 1, Integer.MAX_VALUE);
        spawnCycleIntervalMidnight = spawnCycleIntervalMidnightProperty.getInt();

        Property maxEyesAroundPlayerMidnightProperty = config.get(Categories.spawning, "maxEyesAroundPlayerMidnight", Defaults.maxEyesAroundPlayerMidnight, "Max amount of eyes that will spawn around one player.", 1, Integer.MAX_VALUE);
        maxEyesAroundPlayerMidnight = maxEyesAroundPlayerMidnightProperty.getInt();

        Property maxTotalEyesPerDimensionMidnightProperty = config.get(Categories.spawning, "maxTotalEyesPerDimensionMidnight", Defaults.maxTotalEyesPerDimensionMidnight, "Max number of eyes entities that will spawn in each dimension.", 1, Integer.MAX_VALUE);
        maxTotalEyesPerDimensionMidnight = maxTotalEyesPerDimensionMidnightProperty.getInt();

        Property spawnCycleIntervalHalloweenProperty = config.get(Categories.spawning, "spawnCycleIntervalHalloween", Defaults.spawnCycleIntervalHalloween, "Number of ticks between spawn cycles.", 1, Integer.MAX_VALUE);
        spawnCycleIntervalHalloween = spawnCycleIntervalHalloweenProperty.getInt();

        Property maxEyesAroundPlayerHalloweenProperty = config.get(Categories.spawning, "maxEyesAroundPlayerHalloween", Defaults.maxEyesAroundPlayerHalloween, "Max amount of Eyes that will spawn around one player.", 1, Integer.MAX_VALUE);
        maxEyesAroundPlayerHalloween = maxEyesAroundPlayerHalloweenProperty.getInt();

        Property maxTotalEyesPerDimensionHalloweenProperty = config.get(Categories.spawning, "maxTotalEyesPerDimensionHalloween", Defaults.maxTotalEyesPerDimensionHalloween, "Max number of Eyes entities that will spawn in each dimension.", 1, Integer.MAX_VALUE);
        maxTotalEyesPerDimensionHalloween = maxTotalEyesPerDimensionHalloweenProperty.getInt();

        Property maxEyesSpawnDistanceProperty = config.get(Categories.spawning, "maxEyesSpawnDistance", Defaults.maxEyesSpawnDistance, "Max distance from a player at which Eyes will spawn.", 1, Integer.MAX_VALUE);
        maxEyesSpawnDistance = (float) maxEyesSpawnDistanceProperty.getDouble();


        /* misc */
        Property printPotionsProperty = config.get(Categories.misc, "printPotions", Defaults.printPotions, "If set to true, print a list of potions in the logs on game post init.");
        printPotions = printPotionsProperty.getBoolean();

        Property printMobsProperty = config.get(Categories.misc, "printMobs", Defaults.printMobs, "If set to true, print a list of mob names on game post init.");
        printMobs = printMobsProperty.getBoolean();

        Property damageFromWetProperty = config.get(Categories.misc, "damageFromWet", Defaults.damageFromWet, "Amount of damage eyes get from water.", 0, Float.MAX_VALUE);
        damageFromWet = (float) damageFromWetProperty.getDouble();

        Property spawnCycleSpawnWarningTimeProperty = config.get(Categories.misc, "spawnCycleSpawnWarningTime", Defaults.spawnCycleSpawnWarningTime, "Warning if a spawn cycle takes longer than this amount of milliseconds.", 0, Float.MAX_VALUE);
        spawnCycleSpawnWarningTime = spawnCycleSpawnWarningTimeProperty.getInt();


        /* Mob interactions */
        Property eyesAttackTamedWolvesProperty = config.get(Categories.mob_interactions, "eyesAttackTamedWolves", Defaults.eyesAttackTamedWolves, "Controls whether Eyes attack tamed Wolves.");
        eyesAttackTamedWolves = eyesAttackTamedWolvesProperty.getBoolean();

        Property wolvesAttackEyesProperty = config.get(Categories.mob_interactions, "wolvesAttackEyes", Defaults.wolvesAttackEyes, "Controls whether tamed wolves attack Eyes.");
        wolvesAttackEyes = wolvesAttackEyesProperty.getBoolean();

        Property mobsAttackDormantEyesProperty = config.get(Categories.mob_interactions, "mobsAttackDormantEyes", Defaults.mobsAttackDormantEyes, "Controls whether tamed wolves attack dormant Eyes (wolvesAttackEyes must be true for the attack to happen).");
        mobssAttackDormantEyes = mobsAttackDormantEyesProperty.getBoolean();

        Property mobStringsAttackingProperty = config.get(Categories.mob_interactions, "mobStringsAttackingEyes", Defaults.mobStringsAttackingEyes, "List of mobs that should attack Eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            mobStringsAttackingProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobStringsAttackingEyes = mobStringsAttackingProperty.getStringList();

        Property mobStringsThatEyesAttackProperty = config.get(Categories.mob_interactions, "mobStringsThatEyesAttack", Defaults.mobStringsThatEyesAttack, "List of mobs that should get attacked by Eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            mobStringsThatEyesAttackProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobStringsThatEyesAttack = mobStringsThatEyesAttackProperty.getStringList();

        Property mobStringsFleeingEyesProperty = config.get(Categories.mob_interactions, "mobStringsFleeingEyes", Defaults.mobStringsFleeingEyes, "List of mobs that should flee Eyes.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            mobStringsFleeingEyesProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobStringsFleeingEyes = mobStringsFleeingEyesProperty.getStringList();

        Property mobStringsThatEyesFleeProperty = config.get(Categories.mob_interactions, "mobStringsThatEyesFlee", Defaults.mobStringsThatEyesFlee, "List of mobs that Eyes should flee.");
        if (!Util.isServer() && EyesInTheShadows.varInstanceClient.configMaxxingLoaded) {
            mobStringsThatEyesFleeProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobStringsThatEyesFlee = mobStringsThatEyesFleeProperty.getStringList();

        Property mobsFleeDormantEyesProperty = config.get(Categories.mob_interactions, "mobsFleeDormantEyes", Defaults.mobsFleeDormantEyes, "Controls whether mobs flee dormant eyes.");
        mobsFleeDormantEyes = mobsFleeDormantEyesProperty.getBoolean();

        /* Visual */
        Property blinkDurationProperty = config.get(Categories.visual, "blinkDuration", Defaults.blinkDuration, "Eye blink duration. Set to -1 to disable blinking", -1, Integer.MAX_VALUE);
        blinkDuration = blinkDurationProperty.getInt();

        Property blinkChanceProp = config.get(Categories.visual, "blinkChance", Defaults.blinkChance, "How often Eyes should blink.", 0, 1);
        if (!Util.isServer()) {
            blinkChanceProp.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        }
        blinkChance = (float) blinkChanceProp.getDouble();


        EyesInTheShadows.varInstanceCommon.buildPotionList();
        EyesInTheShadows.varInstanceCommon.buildDimensionList();
        EyesInTheShadows.varInstanceCommon.buildMobLists();
    }
}