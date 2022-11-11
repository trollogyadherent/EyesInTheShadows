package trollogyadherent.eyesintheshadows.entity.entities;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.entity.IModEntity;
import trollogyadherent.eyesintheshadows.packet.PacketHandler;
import trollogyadherent.eyesintheshadows.packet.PacketUtil;
import trollogyadherent.eyesintheshadows.packet.packets.InitiateJumpscarePacket;

import javax.vecmath.Vector3d;
import java.lang.reflect.Method;
import java.util.List;

public class EntityEyes extends EntityMob implements IModEntity {
    private NBTTagCompound syncDataCompound = new NBTTagCompound();

    public boolean blinkingState;
    public int blinkProgress;
    public Method computeLightValueMethod;

    public EntityEyes(World world) {
        super(world);
        /* int computeLightValue(int p_98179_1_, int p_98179_2_, int p_98179_3_, EnumSkyBlock p_98179_4_) */
        computeLightValueMethod = ReflectionHelper.findMethod(net.minecraft.world.World.class, world, new String[]{"computeLightValue", "func_98179_a"}, int.class, int.class, int.class, EnumSkyBlock.class);
        //setSize(1.0F, 0.25F);
        this.setSize(1.0F, 2.0F);
        this.stepHeight = 1.0F;
        initSyncDataCompound();
        setupAI();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.experienceValue = 5;
    }

    private static class CreepTowardPlayer extends EntityAIAttackOnCollide {
        private final EntityEyes eyes;

        public CreepTowardPlayer(EntityCreature creature, double p_i1636_2_, boolean p_i1636_4_) {
            super(creature, p_i1636_2_, p_i1636_4_);
            eyes = (EntityEyes) creature;
        }

        @Override
        public boolean continueExecuting()
        {
            if (isPlayerLookingInMyGeneralDirection(eyes)) {
                return false;
            }
            return super.continueExecuting();
        }

        @Override
        public boolean shouldExecute()
        {
            if (isPlayerLookingInMyGeneralDirection(eyes)) {
                return false;
            }
            return super.shouldExecute();
        }
    }

    static boolean isPlayerLookingInMyGeneralDirection(EntityEyes eyes)
    {
        Vec3 eyesPos = getPosEyes(eyes);
        float blockLight = 0;
            /* TODO
            if (ConfigData.EyesCanAttackWhileLit)
            {
                if (!eyes.worldObj.provider.hasNoSky)
                {
                    float skyLight = eyes.worldObj.getLightFor(EnumSkyBlock.SKY, eyesPos) - (1 - eyes.world.provider.getSunBrightnessFactor(1.0f)) * 11;
                    blockLight = Math.max(blockLight, skyLight);
                }
            } else {
                blockLight = eyes.world.getLight(eyesPos, false);
            }
            if (blockLight >= 8) {
                return true;
            }
            */

        Vector3d selfPos = new Vector3d(eyes.posX, eyes.posY, eyes.posZ);
        EntityLivingBase target = eyes.getAttackTarget();
        if (target == null) {
            return false;
        }
        Vector3d playerPos = new Vector3d(target.posX, target.posY, target.posZ);
        Vec3 lookVec = target.getLookVec();
        Vector3d playerLook = new Vector3d(lookVec.xCoord, lookVec.yCoord, lookVec.zCoord);
        playerLook.normalize();

        playerPos.sub(selfPos);
        playerPos.normalize();

        return playerLook.dot(playerPos) < 0;
    }

    public static Vec3 getPosEyes(EntityEyes eyes) {
        return Vec3.createVectorHelper(eyes.posX, eyes.posY + eyes.getEyeHeight(), eyes.posZ);
    }

    @Override
    public void setupAI() {
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        //this.tasks.addTask(1, new EntityAIRestrictSun(this));
        ///////this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, 0, false));
        //this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, 0, false));
        //this.tasks.addTask(2, new EntityAIFleeSun(this, 1.0D));
        //this.tasks.addTask(7, new CreepTowardPlayer(this, 0.25D, false));

        /*this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAILookIdle(this));*/
    }

    @Override
    public void clearAITasks() {
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
    }



    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.health);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        // store additional custom variables for save, example: par1NBTTagCompound.setBoolean("Angry", isAngry());
    }

    /* (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        // retrieve additional custom variables from save, example: setAngry(par1NBTTagCompound.getBoolean("Angry"));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //EyesInTheShadows.debug("Looking in my dir: " + isPlayerLookingInMyGeneralDirection(this));

        if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.isRemote) {
            if (!getBlinkingState()) {
                if (EyesInTheShadows.rand.nextFloat() < Config.blinkChance / 10) {
                    setBlinkingState(true);
                    setBlinkProgress(0);
                }
            } else {
                setBlinkProgress(getBlinkProgress() + 1);
                if (getBlinkProgress() >= Config.blinkDuration) {
                    setBlinkingState(false);
                }
            }
        } else {/*
            Vec3 position = getPosEyes(this);
            if (Minecraft.getMinecraft().theWorld.getLight(position, false) < 8) {
                float maxWatchDistance = 16;
                Vec3 eyes = getPositionEyes(1);
                List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(eyes.x - maxWatchDistance, eyes.y - maxWatchDistance, eyes.z - maxWatchDistance,
                                eyes.x + maxWatchDistance, eyes.y + maxWatchDistance, eyes.z + maxWatchDistance), (player) -> {

                            if (player.getPositionEyes(1).distanceTo(eyes) > maxWatchDistance)
                                return false;

                            Vec3 vec3d = player.getLook(1.0F).normalize();
                            Vec3 vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double) this.getEyeHeight() - (player.posY + (double) player.getEyeHeight()), this.posZ - player.posZ);
                            double d0 = vec3d1.length();
                            vec3d1 = vec3d1.normalize();
                            double d1 = vec3d.dotProduct(vec3d1);
                            return (d1 > 1.0D - 0.025D / d0) && player.canEntityBeSeen(this);
                        });

                if (entities.size() > 0) {
                    disappear(true);
                }
            }
            */
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {

        // DEBUG!!!!!
        if (par1DamageSource.getEntity() instanceof EntityPlayerMP) {
            jumpscare((EntityPlayerMP) par1DamageSource.getEntity());
        }

        if (isEntityInvulnerable()) {
            return false;
        } else {

            /* if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            } */

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity attackedEntity) {
        boolean jumpScared = Config.jumpscare && attackedEntity instanceof EntityPlayerMP;
        if (jumpScared) {
            jumpscare((EntityPlayerMP) attackedEntity);
        }

        /* jabelar says this is correct :shrug: */
        this.setLastAttacker(attackedEntity);

        // stub return
        return false;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;  //TODO: configurable
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return ticksExisted > 2400; //TODO: configurable
    }

    // *****************************************************
    // ENCAPSULATION SETTER AND GETTER METHODS
    // Don't forget to send sync packets in setters
    // *****************************************************

    @Override
    public void setScaleFactor(float parScaleFactor) {
        syncDataCompound.setFloat("scaleFactor", Math.abs(parScaleFactor));

        // don't forget to sync client and server
        sendEntitySyncPacket();
    }

    @Override
    public float getScaleFactor() {
        return syncDataCompound.getFloat("scaleFactor");
    }

    public void setBlinkingState(boolean value) {
        syncDataCompound.setBoolean("blinkingState", value);
        sendEntitySyncPacket();
    }

    public boolean getBlinkingState() {
        return syncDataCompound.getBoolean("blinkingState");
    }

    public void setBlinkProgress(float value) {
        syncDataCompound.setFloat("blinkProgress", value);
        sendEntitySyncPacket();
    }

    public float getBlinkProgress() {
        return syncDataCompound.getFloat("blinkProgress");
    }

    @Override
    public void sendEntitySyncPacket() {
        PacketUtil.sendEntitySyncPacketToClient(this);
    }

    @Override
    public NBTTagCompound getSyncDataCompound() {
        return syncDataCompound;
    }

    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound)
    {
        syncDataCompound = parCompound;
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.wildanimals.entities.IModEntity#initSyncDataCompound()
     */
    @Override
    public void initSyncDataCompound() {
        syncDataCompound.setFloat("scaleFactor", 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return EyesInTheShadows.varInstanceClient.disappearSound;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer) {
            disappear(true);
        }
        //super.collideWithEntity(entityIn);
    }

    private void disappear(boolean playDeathSound) {
        //damageEntity(DamageSource.generic, 1);
        kill();
        if (playDeathSound) {
            this.playSound(getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void jumpscare(EntityPlayerMP player) {
        IMessage msg = new InitiateJumpscarePacket.SimpleMessage(player.posX, player.posY, player.posZ);
        PacketHandler.net.sendTo(msg, player);
    }

    /* So basically MC draws non-translucent entities first, (pass 0), and then
    * translucent blocks, like water (render pass 1).
    * by default, mobs render on render pass 1. But this one has transparent parts and water behind would
    * look weird. Setting it to one helps a little bit. If there is water between the entity and the camera,
    * the entity will look like if it was before the water, but it's still better than default.
    * https://forums.minecraftforge.net/topic/26876-solvedtransparent-texture-in-entities-without-ruining-water-render/ */
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    protected void onDeathUpdate() {
        /* This makes the death particles spawn instantly, which is more fitting than
        * the standard delay. */
        this.deathTime = 19;
        super.onDeathUpdate();
    }

    /* The whole gimmick is that it should be a ghost-like entity, duh. */
    @Override
    public boolean canBePushed()
    {
        return false;
    }

    public float getEyeHeight()
    {
        //return this.height * EyesInTheShadows.varInstanceClient.hmod;//0.03F;
        return this.height * 0.2F;//EyesInTheShadows.varInstanceClient.hmod;
    }

}
