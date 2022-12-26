package trollogyadherent.eyesintheshadows;

import com.falsepattern.lib.compat.BlockPos;
import com.google.common.base.Stopwatch;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;
import trollogyadherent.eyesintheshadows.util.TimeUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EyesSpawningManager {
    private final Stopwatch watch = Stopwatch.createUnstarted();
    //private final ServerChunkCache chunkSource;
    private int cooldown;
    private int ticks;

    PropertyManager serverPropertyManager;

    public EyesSpawningManager() {
       /* try {
            this.serverPropertyManager = (PropertyManager) EyesInTheShadows.varInstanceCommon.settingsField.get(DedicatedServer.getServer());
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Reflection failed");
            e.printStackTrace();
        }*/
    }

    boolean allowSpawnMonsters() {
        if (this.serverPropertyManager == null) {
            return true;
        }
        return this.serverPropertyManager.getBooleanProperty("spawn-monsters", true);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        //System.out.println("SNEED");
        if (event.side.isClient()) {
            return;
        }
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        tick(event.world);
    }

    private void tick(World world) {
        if (--cooldown > 0) {
            return;
        }

        cooldown = 150;

        if (!Config.enableNaturalSpawn || !world.getGameRules().getGameRuleBooleanValue("doMobSpawning")) {
            return;
        }

        if (!allowSpawnMonsters()) {
            return;
        }

        for (String s : Config.dimensionSpawnNames) {
            if (world.getProviderName().equals(s) && !Config.biomeListIsWhitelist
                    || !world.getProviderName().equals(s) && Config.biomeListIsWhitelist) {
                return;
            }
        }


        try {
            watch.start();

            ticks++;

            int daysUntilNextHalloween = EyesInTheShadows.varInstanceCommon.daysUntilHalloween;
            int minutesToMidnight = TimeUtil.getMinutesToMidnight();

            cooldown = calculateSpawnCycleInterval(daysUntilNextHalloween, minutesToMidnight);

            int maxTotalEyesPerDimension = calculateMaxTotalEyesPerDimension(daysUntilNextHalloween, minutesToMidnight);
            int maxEyesAroundPlayer = calculateMaxEyesAroundPlayer(daysUntilNextHalloween, minutesToMidnight);

            int count = world.countEntities(EntityEyes.class);
            if (count >= maxTotalEyesPerDimension) {
                return;
            }

            float d = Config.maxEyesSpawnDistance * 1.5f;
            float dSqr = d * d;
            AxisAlignedBB size = AxisAlignedBB.getBoundingBox(0, 0, 0, d, d, d); //.ofSize(Vec3.ZERO, d, d, d);

            List<EntityPlayerMP> players = world.playerEntities;
            int wrap = Math.min(players.size(), 20);
            for (EntityPlayerMP player : players)
            {
                if (((player.getEntityId() + ticks) % wrap) == 0 && !player.capabilities.isCreativeMode)
                {
                    List<EntityEyes> entities = world.getEntitiesWithinAABB(EntityEyes.class, size); //world.getEntities(EyesInTheDarkness.EYES.get(), size.move(player.position()), e -> !e.countsTowardSpawnCap() && e.distanceToSqr(player) <= dSqr);
                    if (entities.size() < maxEyesAroundPlayer)
                    {
                        spawnOneAround(Vec3.createVectorHelper(player.posX, player.posY, player.posZ), player, Config.maxEyesSpawnDistance);
                    }
                }
            }

        }
        finally {
            watch.stop();

            long us = watch.elapsed(TimeUnit.MICROSECONDS);
            if (us > 100) // default = 50ms{
                EyesInTheShadows.warn("WARNING: Unexpectedly long spawn cycle. It ran for " + us/1000.0 + "ms!");
            }

            watch.reset();
        }

    private int calculateSpawnCycleInterval(int daysUntilNextHalloween, int minutesToMidnight)
    {
        return Math.max(1, calculateTimeBasedValueMin(Config.spawnCycleIntervalNormal, Config.spawnCycleIntervalMidnight, Config.spawnCycleIntervalHalloween, daysUntilNextHalloween, minutesToMidnight));
    }

    private int calculateMaxTotalEyesPerDimension(int daysUntilNextHalloween, int minutesToMidnight) {
        return Math.max(1, calculateTimeBasedValueMax(Config.maxTotalEyesPerDimensionNormal, Config.maxTotalEyesPerDimensionMidnight, Config.maxTotalEyesPerDimensionHalloween, daysUntilNextHalloween, minutesToMidnight));
    }

    private int calculateMaxEyesAroundPlayer(int daysUntilNextHalloween, int minutesToMidnight) {
        return Math.max(1, calculateTimeBasedValueMax(Config.maxEyesAroundPlayerNormal, Config.maxEyesAroundPlayerMidnight, Config.maxEyesAroundPlayerHalloween, daysUntilNextHalloween, minutesToMidnight));
    }

    private int calculateTimeBasedValueMax(int normal, int midnight, int halloween, int daysUntilNextHalloween, int minutesToMidnight) {
        int valueByTime = normal + ((midnight - normal) * Math.max(0, 240 - minutesToMidnight)) / 240;
        int valueByDate = normal + ((halloween - normal) * Math.max(0, 30 - daysUntilNextHalloween)) / 30;
        return Math.max(valueByDate, valueByTime);
    }

    private int calculateTimeBasedValueMin(int normal, int midnight, int halloween, int daysUntilNextHalloween, int minutesToMidnight) {
        int valueByTime = normal + ((midnight - normal) * Math.max(0, 240 - minutesToMidnight)) / 240;
        int valueByDate = normal + ((halloween - normal) * Math.max(0, 30 - daysUntilNextHalloween)) / 30;
        return Math.min(valueByDate, valueByTime);
    }

    private void spawnOneAround(Vec3 positionVec, EntityPlayerMP player, float d) {
        float dSqr = d*d;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int i=0;i<100;i++) {
            double sX = (1 - 2 * EyesInTheShadows.varInstanceCommon.rand.nextFloat()) * d + positionVec.xCoord;
            double sZ = (1 - 2 * EyesInTheShadows.varInstanceCommon.rand.nextFloat()) * d + positionVec.zCoord;

            pos.setPos(sX, 255, sZ);

            double sY = MathHelper.clamp_int((int) (EyesInTheShadows.varInstanceCommon.rand.nextFloat() * d + positionVec.yCoord), 0, 255);
            pos.setY((int) sY);

            double pX = pos.getX() + 0.5D;
            double pY = pos.getY();
            double pZ = pos.getZ() + 0.5D;

            double distanceSq = player.getDistanceSq(pX, pY, pZ);
            EntityEyes entity = new EntityEyes(player.worldObj);
            if (distanceSq < dSqr && isValidSpawnSpot(player.worldObj, entity, pos, distanceSq)) {
                //EyesInTheDarkness.EYES.get().create(player.worldObj, null, null, null, pos, MobSpawnType.NATURAL, false, false);
                if (entity == null)
                    continue;

                /*int canSpawn = net.minecraftforge.common.ForgeHooks. canEntitySpawn(entity, player.worldObj, pX, pY, pZ, null, MobSpawnType.NATURAL);
                if (canSpawn != -1 && (canSpawn == 1 || entity.checkSpawnRules(player.worldObj, MobSpawnType.NATURAL) && entity.checkSpawnObstruction(player.worldObj)))
                {
                    player.worldObj.addFreshEntity(entity);

                    return;
                }*/

                entity.setPosition(pX, pY, pZ);
                player.worldObj.spawnEntityInWorld(entity);

                return;
            }
            entity.setDead();
        }
    }

    private static boolean isValidSpawnSpot(World serverWorld, EntityLivingBase entity, BlockPos.MutableBlockPos pos, double sqrDistanceToClosestPlayer) {
        int instantDespawnDistance = 1024; /* EntityLiving, despawnEntity() */
        if (sqrDistanceToClosestPlayer > (instantDespawnDistance * instantDespawnDistance)) {
            return false;
        }

        /*if (!BiomeRules.isBiomeAllowed(serverWorld, serverWorld.getBiome(pos)))
        {
            return false;
        }*/

        //return SpawnPlacements.checkSpawnRules(entity, serverWorld, MobSpawnType.NATURAL, pos, serverWorld.random)
        //      && serverWorld.noCollision(entity.getAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));

        return SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, serverWorld, pos.getX(), pos.getY(), pos.getZ());
    }
}
