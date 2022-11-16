package trollogyadherent.eyesintheshadows.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

public class JumpscareOverlay extends Gui
{
    private static final ResourceLocation TEXTURE_EYES = EyesInTheShadows.varInstanceClient.eyes2Texture;
    private static final ResourceLocation TEXTURE_FLASH = EyesInTheShadows.varInstanceClient.creepyTexture;

    public static JumpscareOverlay INSTANCE = new JumpscareOverlay();

    private boolean visible = false;
    private float progress = 0;

    private Minecraft mc;

    private static final Rectangle[] FRAMES = {
            new Rectangle(0,0,13,6),
            new Rectangle(0,7,13,6),
            new Rectangle(0,14,13,6),
            new Rectangle(0,21,13,6),
            new Rectangle(15,1,15,8),
            new Rectangle(15,16,15,12),
    };
    private static final int ANIMATION_APPEAR = 10;
    private static final int ANIMATION_LINGER = 90;
    private static final int ANIMATION_BLINK = 60;
    private static final int ANIMATION_SCARE1 = 20;
    private static final int ANIMATION_FADE = 20;
    private static final int ANIMATION_BLINK_START = ANIMATION_APPEAR + ANIMATION_LINGER;
    private static final int ANIMATION_SCARE_START = ANIMATION_BLINK_START + ANIMATION_BLINK;
    private static final int ANIMATION_FADE_START = ANIMATION_SCARE_START + ANIMATION_SCARE1;
    private static final int ANIMATION_TOTAL = ANIMATION_APPEAR + ANIMATION_LINGER + ANIMATION_BLINK
            + ANIMATION_SCARE1 + ANIMATION_FADE;

    private JumpscareOverlay()
    {
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public void show(double ex, double ey, double ez)
    {
        visible = true;
        mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(EyesInTheShadows.varInstanceCommon.jumpScareSound), 1.0F));
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event)
    {
        if (visible)
        {
            progress++;
            if (progress >= ANIMATION_TOTAL)
            {
                visible = false;
                progress = 0;
            }
        }
    }

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent.Pre event)
    {
        if (!visible || event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        int screenWidth = mc.displayWidth;
        int screenHeight = mc.displayHeight;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glClear(256);
        //GlStateManager.clear(256);
        GL11.glMatrixMode(5889);
        //GlStateManager.matrixMode(5889);
        GL11.glLoadIdentity();
        //GlStateManager.loadIdentity();
        GL11.glOrtho(0.0D, screenWidth, screenHeight, 0.0D, 1000.0D, 3000.0D);
        //GlStateManager.ortho(0.0D, screenWidth, screenHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        //GlStateManager.matrixMode(5888);
        GL11.glLoadIdentity();
        //GlStateManager.loadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        //GlStateManager.translate(0.0F, 0.0F, -2000.0F);


        //GL11.glEnable(GL11.GL_ALPHA_TEST);

        float time = progress + event.partialTicks;
        if (time >= ANIMATION_TOTAL)
        {
            visible = false;
            progress = 0;
            return;
        }

        float darkening = MathHelper.clamp_float(Math.min(time/ ANIMATION_APPEAR, (ANIMATION_TOTAL - time) / ANIMATION_FADE), 0, 1);

        boolean showCreep = false;
        int blinkstate = 0;
        if (time >= ANIMATION_BLINK_START)
        {
            if(time >= ANIMATION_SCARE_START)
            {
                blinkstate = 1;
                showCreep = (time - ANIMATION_SCARE_START) > ANIMATION_SCARE1;
            }
            else
            {
                float fade = Math.max(0, (time - ANIMATION_BLINK_START) / ANIMATION_BLINK);
                float blinkspeed = (float) (1 + Math.pow(fade, 3));
                blinkstate = MathHelper.floor_float(20 * blinkspeed) & 1;
                showCreep = blinkstate == 1;
            }
        }

        int alpha = MathHelper.floor_double(darkening * 255);

        if (showCreep)
        {
            int texW = 2048;
            int texH = 1024;

            float scale1 = screenHeight / (float)texH;
            int drawY = 0;
            int drawH = screenHeight;
            int drawW = MathHelper.floor_float(texW * scale1);
            int drawX = (screenWidth-drawW)/2;
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glColor4f(1, 1, 1, alpha);

            drawScaledCustomTexture(TEXTURE_FLASH, texW, texH, 0, 0, texW, texH, drawX, drawY, drawW, drawH, (alpha << 24) | 0xFFFFFF);
            GL11.glFlush();
        }
        else
        {
            drawRect(0,0, screenWidth, screenHeight, alpha << 24);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
        }

        if (blinkstate == 1)
        {
            GL11.glPopAttrib();
            return;
        }

        float scale = Float.MAX_VALUE;
        for(Rectangle r : FRAMES)
        {
            float s = Math.min(
                    MathHelper.floor_double(screenWidth * 0.8 / (float)r.getWidth()),
                    MathHelper.floor_double(screenHeight * 0.8 / (float)r.getHeight()));
            scale = Math.min(scale, s);
        }

        scale = Math.min(1, (1+time)/(1+ ANIMATION_APPEAR)) * scale;

        int currentFrame = Math.min(FRAMES.length-1, MathHelper.floor_float(FRAMES.length * time / ANIMATION_APPEAR));

        Rectangle rect = FRAMES[currentFrame];
        int tx = rect.getX();
        int ty = rect.getY();
        int tw = rect.getWidth();
        int th = rect.getHeight();

        float drawW = (tw) * scale;
        float drawH = (th) * scale;
        float drawX = ((screenWidth - drawW)/2.0f);
        float drawY = ((screenHeight - drawH)/2.0f);

        float texW = 32;
        float texH = 32;
        drawScaledCustomTexture(TEXTURE_EYES, texW, texH, tx, ty, tw, th, drawX, drawY, drawW, drawH);

        GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glPopAttrib();
    }

    private void drawScaledCustomTexture(ResourceLocation tex, float texW, float texH, int tx, int ty, int tw, int th, float targetX, float targetY, float targetW, float targetH)
    {
        mc.renderEngine.bindTexture(tex);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(targetX, targetY, 0, tx / texW, ty / texH);
        tessellator.addVertexWithUV(targetX, targetY + targetH, 0, tx / texW, (ty + th) / texH);
        tessellator.addVertexWithUV(targetX + targetW, targetY + targetH, 0, (tx + tw) / texW, (ty + th) / texH);
        tessellator.addVertexWithUV(targetX + targetW, targetY, 0, (tx + tw) / texW, ty / texH);

        tessellator.draw();
    }

    private void drawScaledCustomTexture(ResourceLocation tex, float texW, float texH, int tx, int ty, int tw, int th, float targetX, float targetY, float targetW, float targetH, int color)
    {
        int a = (color >> 24)&255;
        int r = (color >> 16)&255;
        int g = (color >> 8)&255;
        int b = (color >> 0)&255;

        mc.renderEngine.bindTexture(tex);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        //GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator tessellator = Tessellator.instance;

        //EyesInTheShadows.debug("Showing creep, alpha: " + a);

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(r, g, b, a);
        tessellator.addVertexWithUV(targetX, targetY, 0, tx / texW, ty / texH);
        tessellator.addVertexWithUV(targetX, targetY + targetH, 0, tx / texW, (ty + th) / texH);
        tessellator.addVertexWithUV(targetX + targetW, targetY + targetH, 0, (tx + tw) / texW, (ty + th) / texH);
        tessellator.addVertexWithUV(targetX + targetW, targetY, 0, (tx + tw) / texW, ty / texH);

        tessellator.draw();

        GL11.glPopAttrib();
    }
}
