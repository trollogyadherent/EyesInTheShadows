package trollogyadherent.eyesintheshadows.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;
import trollogyadherent.eyesintheshadows.util.Util;

import java.lang.reflect.InvocationTargetException;

public class ModelEyes extends ModelBase {
    public ModelRenderer head;
    public int textureWidth = 32;
    public int textureHeight = 32;
    ResourceLocation texture;

    int lastPressedKey;

    // create an animation cycle
    // for movement based animations you need to measure distance moved
    // and perform number of cycles per block distance moved.
    protected double distanceMovedTotal = 0.0D;

    // don't make this too large or animations will be skipped


    protected static final double CYCLES_PER_BLOCK = 3.0D;

    public ModelEyes() {
        head = new ModelRenderer(this, 120, 420);
        //head.addBox(-32F, -32F, 0F, 32, 32, 0);
        head.addBox(100F, 100F, 100F, 100, 132, 100);
        head.setRotationPoint(16F, 22F, 0F);
        head.setTextureSize(textureWidth, textureHeight);
        setRotation(head, 0F, 0F, 0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity parEntity, float parTime, float parSwingSuppress, float par4, float parHeadAngleY, float parHeadAngleX, float par7)
    {
        // best to cast to actual expected entity, to allow access to custom fields

        // related to animation
        renderEyes((EntityEyes) parEntity, parTime, parSwingSuppress, par4, parHeadAngleY, parHeadAngleX, par7);
        super.render(parEntity, parTime, parSwingSuppress, par4, parHeadAngleY, parHeadAngleX, par7);
    }

    public void renderEyes(EntityEyes parEntity, float parTime, float parSwingSuppress, float par4, float parHeadAngleY, float parHeadAngleX, float par7)
    {
        if (!parEntity.isEntityAlive()) {
            return;
        }

        setRotationAngles(parTime, parSwingSuppress, par4, parHeadAngleY, parHeadAngleX, par7, parEntity);

        /*int xmod = 1;
        int ymod = 0;
        int zmod = 0;
        int blockLight = Minecraft.getMinecraft().theWorld.getFullBlockLightValue((int) parEntity.posX + xmod, (int) parEntity.posY + ymod, (int) parEntity.posZ + zmod);
        Block b = Minecraft.getMinecraft().theWorld.getBlock((int) parEntity.posX + xmod, (int) parEntity.posY + ymod, (int) parEntity.posZ + zmod);
        blockLight = b.getLightValue(); */


        /*int coreX = MathHelper.floor_double(parEntity.posX);
        int coreY = MathHelper.floor_double(parEntity.posY);
        int coreZ = MathHelper.floor_double(parEntity.posZ);
        int totalLight = 0;
        int totalBlocks = 0;
        for (int x = coreX - 5; x <= coreX + 5; x ++)
        {
            for (int y = coreY - 5; y <= coreY + 5; y ++)
            {
                for (int z = coreZ - 5; z <= coreZ + 5; z ++)
                {
                    if (!Minecraft.getMinecraft().theWorld.getBlock(x, y, z).getUseNeighborBrightness())
                    {
                        totalLight += Minecraft.getMinecraft().theWorld.getFullBlockLightValue(x, y, z);
                        totalBlocks += 1;
                    }
                }
            }
        }
        int blockLight = totalLight / totalBlocks;*/

        int x = MathHelper.floor_double(parEntity.posX) + EyesInTheShadows.varInstanceClient.xmod;
        int y = MathHelper.floor_double(parEntity.posY) + EyesInTheShadows.varInstanceClient.ymod;
        int z = MathHelper.floor_double(parEntity.posZ) + EyesInTheShadows.varInstanceClient.zmod;

        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            lastPressedKey = Keyboard.KEY_C;
        } else if (lastPressedKey == Keyboard.KEY_C) {
            lastPressedKey = -1;

            Block b = parEntity.worldObj.getBlock(x, y, z);
            EyesInTheShadows.debug("Block name: " + b.getLocalizedName() + ", x: " + x + ", y: " + y + ", z :" + z);
            EyesInTheShadows.debug("Block lightvalue: " + b.getLightValue());
            /* seems like the winner */ EyesInTheShadows.debug("world.getBlockLightValue: " + parEntity.worldObj.getBlockLightValue(x, y, z));
            EyesInTheShadows.debug("world.getBlockLightValue_do: " + parEntity.worldObj.getBlockLightValue_do(x, y, z, false));
            EyesInTheShadows.debug("world.getFullBlockLightValue: " + parEntity.worldObj.getFullBlockLightValue(x, y, z));
            EyesInTheShadows.debug("world.getLightBrightness: " + parEntity.worldObj.getLightBrightness(x, y, z));
            EyesInTheShadows.debug("world.getSavedLightValue: " + parEntity.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z));
            EyesInTheShadows.debug("b.getLightOpacity: " + b.getLightOpacity());
            EyesInTheShadows.debug("b.getMixedBrightnessForBlock: " + b.getMixedBrightnessForBlock(parEntity.worldObj, x, y, z));
            //parEntity.worldObj.com
            try {
                EyesInTheShadows.debug("computeLightValueMethod (block): " + parEntity.computeLightValueMethod.invoke(parEntity.worldObj, x, y, z, EnumSkyBlock.Block));
                EyesInTheShadows.debug("computeLightValueMethod (sky): " + parEntity.computeLightValueMethod.invoke(parEntity.worldObj, x, y, z, EnumSkyBlock.Sky));
            } catch (IllegalAccessException | InvocationTargetException e) {
                EyesInTheShadows.error("Failed to invoke reflected method 'computeLightValueMethod'");
            }

            System.out.println("xxxxxxxxx");
            System.out.println(parEntity.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z));
            System.out.println(parEntity.worldObj.skylightSubtracted);
            System.out.println(parEntity.getBrightness(0.F));
            System.out.println("getAverageLightLevel: " + Util.getAverageLightLevel(parEntity));
            Util.getSunBrightness(parEntity.worldObj);
            System.out.println("xxxxxxxxx");

            EyesInTheShadows.debug("player yaw: " + RenderManager.instance.playerViewY);
            EyesInTheShadows.debug("eyes yaw head: " + parEntity.rotationYawHead);
            EyesInTheShadows.debug("eyes yaw: " + parEntity.rotationYaw);
            EyesInTheShadows.debug("===================model==================");
        }

        //int blockLight = parEntity.worldObj.getBlockLightValue(x, y, z);
        //if (parEntity.worldObj.provider.sky)

        //float mixAlpha = Util.clamp((8 - blockLight) / 8.0f, 0, 1);
        //System.out.println(mixAlpha);

        //float mixAlpha = 1F - parEntity.getBrightness(0.F);//1;

        /*if (Util.isSunVisible(parEntity.worldObj, x, y, z)) {
            mixAlpha = 0;
        }*/

        //7boolean isday = parEntity.worldObj.isDaytime();
        //int saved_lv = parEntity.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z);

        //if (!parEntity.worldObj.provider.hasNoSky && !isday && saved_lv < 9) {
        //    mixAlpha = 1;
        //}

        /* A value from 0 to 1 telling how transparent the eyes will be (the more light there is, the more transparent
            the eyes should be
        ) */
        float mixAlpha;

        /* A value of 15 here means the block is exposed to the sky */
        if (parEntity.worldObj.getBlockLightValue(x, y, z) == 15) {
            /* If the entity is exposed to the sky, we subtract the sun brightness and the brightness from torches/other light emitting blocks */
            mixAlpha = 1 - Util.getSunBrightness(parEntity.worldObj) - Util.getLightSourceBrightness(parEntity.worldObj, x, y, z);
        } else {
            /* Otherwise, we subtract the light emitted by nearby blocks * 1.1, and the getBrightness * sun brightness.
            * getBrightness calculates how much the entity receives light from the sky. Then we multiply because the more day it is, the more we want to actually "enforce it".
            * It blends pretty well.
            *  */
            mixAlpha = 1 - Util.getLightSourceBrightness(parEntity.worldObj, x, y, z) * 1.1F - (parEntity.getBrightness(1.0F) * Util.getSunBrightness(parEntity.worldObj));
        }

        if (mixAlpha <= 0) {
            return;
        }

        // scale the whole thing for big or small entities
        GL11.glPushMatrix();
        ///GL11.glPushAttrib(GL11.GL_ENABLE_BIT);


        GL11.glScalef(parEntity.getScaleFactor(), parEntity.getScaleFactor(), parEntity.getScaleFactor());
        GL11.glTranslated(0, parEntity.getEyeHeight() - 0.75F, 0);

        float viewerYaw = RenderManager.instance.playerViewY;
        float viewerPitch = RenderManager.instance.playerViewX;
        GL11.glRotatef(viewerYaw - parEntity.renderYawOffset, 0, 1, 0);
        GL11.glRotatef(viewerPitch, 1, 0, 0);

        GL11.glRotatef(180F, 1, 0, 0);

        //GL11.glRotatef(Minecraft.getMinecraft().thePlayer.rotationYawHead - parEntity.rotationYaw, 0, 1, 0);

        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        GL11.glColor4f(1, 1, 1, mixAlpha);
        //Minecraft.getMinecraft().entityRenderer.  // something with fog color


        Tessellator tessellator = Tessellator.instance;

        Minecraft.getMinecraft().getTextureManager().bindTexture(EyesInTheShadows.varInstanceClient.eyesTexture);

        final float w = 0.25f;
        final float h = w * 5 / 13f;

        final float tw = 13 / 32f;
        final float th = 5 / 32f;
        float hoff = getBlinkTextureOffset(parEntity, parTime, th);


        tessellator.startDrawingQuads();
        //tessellator.setNormal(0.0F, 0.0F, 1.0F);


        /* 1 */ tessellator.addVertexWithUV(-5 * w, -5 * h, 0, 0, hoff + th);
        /* 2 */ tessellator.addVertexWithUV(-5 * w, 5 * h, 0, 0, hoff);
        /* 3 */ tessellator.addVertexWithUV(5 * w, 5 * h, 0, tw, hoff);
        /* 4 */ tessellator.addVertexWithUV(5 * w, -5 * h, 0, tw, hoff + th);


        tessellator.draw();

        ////head.render(par7);

        //GL11.glPopAttrib();
        // don't forget to pop the matrix for overall scaling
        GL11.glPopMatrix();

        // reset fog color
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0x00F0, 0x00F0);
        GL11.glDisable(GL11.GL_BLEND);
        //GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void setRotationAngles(float parTime, float parSwingSuppress, float par3, float parHeadAngleY, float parHeadAngleX, float par6, Entity parEntity)
    {
        // animate if moving
        updateDistanceMovedTotal(parEntity);
        //super.setRotationAngles(parTime, parSwingSuppress, par3, parHeadAngleY, parHeadAngleY, par6, parEntity);
    }

    protected void updateDistanceMovedTotal(Entity parEntity)
    {
        distanceMovedTotal += parEntity.getDistance(parEntity.prevPosX, parEntity.prevPosY, parEntity.prevPosZ);
    }

    protected double getDistanceMovedTotal(Entity parEntity)
    {
        return (distanceMovedTotal);
    }

    protected float degToRad(float degrees)
    {
        return degrees * (float)Math.PI / 180 ;
    }

    protected void setRotation(ModelRenderer model, float rotX, float rotY, float rotZ)
    {
        model.rotateAngleX = degToRad(rotX);
        model.rotateAngleY = degToRad(rotY);
        model.rotateAngleZ = degToRad(rotZ);
    }

    // spin methods are good for testing and debug rotation points and offsets in the model
    protected void spinX(ModelRenderer model)
    {
        model.rotateAngleX += degToRad(0.5F);
    }

    protected void spinY(ModelRenderer model)
    {
        model.rotateAngleY += degToRad(0.5F);
    }

    protected void spinZ(ModelRenderer model)
    {
        model.rotateAngleZ += degToRad(0.5F);
    }

    private float getBlinkTextureOffset(EntityEyes entity, float partialTicks, float th) {
        float hoff = 0;
        /* For some reason partial ticks goe ridiculously high when the eyes move in the world */
        partialTicks = 0;
        if (entity.getBlinkingState()) {
            int half_blink = Config.blinkDuration / 2;
            if (entity.getBlinkProgress() < half_blink) {
                hoff = MathHelper.floor_float((entity.getBlinkProgress() + partialTicks) * 4f / half_blink) * th;
            } else {
                hoff = Math.max(0, 8 - MathHelper.floor_float((entity.getBlinkProgress() + partialTicks) * 4f / half_blink)) * th;
            }
        }
        return hoff;
    }
}
