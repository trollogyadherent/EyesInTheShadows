package trollogyadherent.eyesintheshadows.entity.entities;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.aitasks.AvoidOcelots;
import trollogyadherent.eyesintheshadows.aitasks.CreepTowardPlayer;
import trollogyadherent.eyesintheshadows.aitasks.EyesWander;
import trollogyadherent.eyesintheshadows.aitasks.TargetTamedWolves;
import trollogyadherent.eyesintheshadows.entity.IModEntity;
import trollogyadherent.eyesintheshadows.packet.PacketHandler;
import trollogyadherent.eyesintheshadows.packet.PacketUtil;
import trollogyadherent.eyesintheshadows.packet.packets.InitiateJumpscarePacket;
import trollogyadherent.eyesintheshadows.util.Util;

import javax.vecmath.Vector3d;
import java.util.List;

public class EntityEyes extends EntityMob implements IModEntity {
    private NBTTagCompound syncDataCompound = new NBTTagCompound();

    public EntityEyes(World world) {
        super(world);
        //setSize(1.0F, 0.25F);
        this.setSize(1.0F, 2.0F);
        this.stepHeight = 1.0F;
        /* Setting aggro level depending on difficulty, if enabled in config */
        if (Config.eyeAggressionDependsOnLocalDifficulty) {
            setAggroLevel(EyesInTheShadows.varInstanceCommon.rand.nextFloat() * worldObj.difficultySetting.getDifficultyId() / 10);
        }
        initSyncDataCompound();
        setupAI();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.experienceValue = 5;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public boolean isPlayerLookingInMyGeneralDirection()
    {
        if (this.getBrightness() <= 0) {
            return false;
        }

        Vector3d selfPos = new Vector3d(this.posX, this.posY, this.posZ);
        EntityLivingBase target = this.getAttackTarget();
        if (target == null) {
            return false;
        }

        if (target.getActivePotionEffect(Potion.blindness) != null) {
            return false;
        }

        if (target.getActivePotionEffect(Potion.invisibility) != null) {
            return true;
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
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(3, new AvoidOcelots(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.targetTasks.addTask(2, new TargetTamedWolves(this, EntityWolf.class, 0, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityWolf.class, 1.0D, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, 0, false));
        this.tasks.addTask(1, new CreepTowardPlayer(this, 0.25D, false));
        this.tasks.addTask(5, new EyesWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAILookIdle(this));

        /* this.tasks.addTask(1, new EntityAISwimming(this));
          this.tasks.addTask(1, new EntityAIRestrictSun(this));
          this.tasks.addTask(2, new EntityAIFleeSun(this, 1.0D));
        */
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
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(Config.eyeBaseAttackDamage);
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
        float alpha = Util.getEyeRenderingAlpha(this, Config.eyesCanAttackWhileLit);

        setEyeBrightness(alpha);
        //EyesInTheShadows.debug("Looking in my dir: " + isPlayerLookingInMyGeneralDirection(this));

        //if(worldObj != null && worldObj.isRemote) {
            if (!getBlinkingState()) {
                if (EyesInTheShadows.varInstanceCommon.rand.nextFloat() < Config.blinkChance / 10) {
                    setBlinkingState(true);
                    setBlinkProgress(0);
                }
            } else {
                setBlinkProgress(getBlinkProgress() + 1);
                if (getBlinkProgress() >= Config.blinkDuration) {
                    setBlinkingState(false);
                }
            }
        //}
        if(worldObj != null && alpha > 0) {
            Vec3 eyePosEyes = Vec3.createVectorHelper(posX, posY + 1, posZ);//getPosEyes(this);
            if (getBrightness() > 0) {
                float maxWatchDistance = Config.watchDistance;
                //eyePosEyes.yCoord += 2;
                List<EntityPlayer> entities = worldObj.getEntitiesWithinAABB(EntityPlayer.class,
                         AxisAlignedBB.getBoundingBox(
                                 eyePosEyes.xCoord - maxWatchDistance, eyePosEyes.yCoord - maxWatchDistance, eyePosEyes.zCoord - maxWatchDistance,
                                eyePosEyes.xCoord + maxWatchDistance, eyePosEyes.yCoord + maxWatchDistance, eyePosEyes.zCoord + maxWatchDistance));


                boolean shouldDisappear = false;
                for (EntityPlayer player : entities) {
                    Vec3 playerPosEyes = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
                    if (playerPosEyes.distanceTo(eyePosEyes) > maxWatchDistance) {
                        shouldDisappear = false;
                        continue;
                    }

                    Vec3 vec3 = player.getLook(1.0F).normalize();
                    Vec3 vec31 = Vec3.createVectorHelper(this.posX - player.posX, this.boundingBox.minY + (double)(this.height /*/ 2.0F*/ - getEyeHeight()) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
                    double d0 = vec31.lengthVector();
                    vec31 = vec31.normalize();
                    double d1 = vec3.dotProduct(vec31);
                    shouldDisappear = d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(this);
                }

                if (shouldDisappear) {
                    disappear(true);
                }
            }
        }

        if (Config.enableEyeAggressionEscalation && alpha > 0)
        {
            if (!isPlayerLookingInMyGeneralDirection()) {
                setAggroLevel(getAggroLevel() + Config.aggroEscalationPerTick);
                if (Config.eyeAggressionDependsOnLightLevel) {
                    setAggroLevel(getAggroLevel() + Config.aggroEscalationPerTick * alpha);
                }
            } else {
                setAggroLevel(getAggroLevel() - Config.aggroEscalationPerTick * 2);
                /*if (Config.eyeAggressionDependsOnLightLevel) {
                    setAggroLevel(getAggroLevel() - Config.aggroEscalationPerTick * alpha);
                }*/
            }
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
        if (par1DamageSource.getEntity() instanceof EntityPlayerMP && EyesInTheShadows.isDebugMode()) {
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
            disappear(false);
        }

        if (this.attackTime == 0) {
            this.attackTime = Config.tickBetweenAttacks;
            super.attackEntityAsMob(attackedEntity);

            /* jabelar says this is correct :shrug: */
            this.setLastAttacker(attackedEntity);
        }

        // stub return
        return false;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return Config.maxSpawnedInChunk;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return ticksExisted > Config.despawnAfterAmountOfTicks;
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

    public void setEyeBrightness(float brightness) {
        syncDataCompound.setFloat("brightness", brightness);
        sendEntitySyncPacket();
    }

    public float getBrightness() {
        return syncDataCompound.getFloat("brightness");
    }

    public void setAggroLevel(float aggro) {
        syncDataCompound.setFloat("aggro", MathHelper.clamp_float(aggro, 0, 1));
        sendEntitySyncPacket();
    }

    public float getAggroLevel() {
        return syncDataCompound.getFloat("aggro");
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

    public double getSpeedFromAggro()
    {
        if (Util.getEyeRenderingAlpha(this, Config.eyesCanAttackWhileLit) <= 0) {
            return 0;
        }
        return Config.speedNoAggro + (Config.speedFullAggro - Config.speedNoAggro) * getAggroLevel();
        //return Util.clampedLerp(getAggroLevel(), Config.speedNoAggro, Config.speedFullAggro);
    }

    @Override
    protected String getDeathSound() {
        return EyesInTheShadows.varInstanceCommon.disappearSound;
    }

    @Override
    protected String getLivingSound() {
        return EyesInTheShadows.varInstanceCommon.laughSound;
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

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


}
