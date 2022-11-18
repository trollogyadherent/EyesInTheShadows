package trollogyadherent.eyesintheshadows.gui.configpickers.mob;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* The gui presenting the list of potions to choose from */
public class MobSelectionGui extends GuiScreen {
    GuiEditArrayMobString.ReturnInfo returnInfo;
    int index;
    private List availablePotions;
    private String status = "Please select a potion:";
    private AvailableMobListGui availableMobListGui;
    int originalValue;
    ResourceLocation potionResourceLocation = new ResourceLocation("textures/items/potion_bottle_empty.png");
    ResourceLocation potionSplashResourceLocation = new ResourceLocation("textures/items/potion_bottle_splash.png");
    ResourceLocation potionOverlayResourceLocation = new ResourceLocation("textures/items/potion_overlay.png");

    public MobSelectionGui(GuiEditArrayMobString parentScreen, Object[] beforeValues, Object[] currentValues, int index, GuiEditArrayMobString.ReturnInfo returnInfo) {


        this.returnInfo = returnInfo;
        this.index = index;
        this.mc = Minecraft.getMinecraft();

        EyesInTheShadows.debug("currentValuesReflected:");
        EyesInTheShadows.debug(Arrays.toString(parentScreen.currentValuesReflected));

        EyesInTheShadows.debug("currentValues passed as args:");
        EyesInTheShadows.debug(Arrays.toString(currentValues));

        this.availablePotions = new ArrayList();
        //this.status = I18n.format("offlineauth.skingui.select_skin");

        System.out.println(returnInfo.values[index]);
        if (returnInfo.values[index] instanceof String) {
            this.originalValue = Integer.parseInt((String) returnInfo.values[index]);
        } else {
            this.originalValue = (int)returnInfo.values[index];
        }

        for (Potion p : Potion.potionTypes) {
            if (p != null) {
                MobListEntry entry = new MobListEntry(this, p);
                this.availablePotions.add(entry);
            }
        }

        this.availableMobListGui = new AvailableMobListGui(this.mc, 410, this.height, 36, this.availablePotions, returnInfo, index);
        //this.availablePotionListGui.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableMobListGui.setSlotXBoundsFromLeft(this.width / 2);
        //this.availablePotionListGui.setS
        this.availableMobListGui.registerScrollButtons(7, 8);
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        //list.add(new DummyConfigElement<>("modIDSelector", "FML", ConfigGuiType.MOD_ID, "bruh sneed"));
        return list;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(3, 20, this.height - 25, (this.width - 25) / 4, 20, I18n.format("gui.done")));
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            EyesInTheShadows.debug("hit escape");
            //mc.displayGuiScreen(new GuiEditArrayPotionID(parentScreen.owningScreen, configElement, slotIndex, currentValues, enabled()));
            //this.mc.displayGuiScreen(parentScreen);
            returnInfo.values[index] = originalValue;
            mc.displayGuiScreen(new GuiEditArrayMobString(returnInfo.parentScreen, returnInfo.configElement, returnInfo.slotIndex, returnInfo.values, returnInfo.enabled));
        } else {
            EyesInTheShadows.debug(String.valueOf(index));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 3)
        {
            mc.displayGuiScreen(new GuiEditArrayMobString(returnInfo.parentScreen, returnInfo.configElement, returnInfo.slotIndex, returnInfo.values, returnInfo.enabled));
        }
    }


    public boolean hasSkinEntry(MobListEntry mobListEntry)
    {
        return this.availablePotions.contains(mobListEntry);
    }

    public List probablyToRemove(MobListEntry skinListEntry)
    {
        return this.hasSkinEntry(skinListEntry) ? this.availablePotions : null;
    }

    public List getAvailablePotions()
    {
        return this.availablePotions;
    }


    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.availableMobListGui.func_148179_a(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        //super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawBackground(0);

        this.availableMobListGui.top = 35;
        this.availableMobListGui.left = 25;
        this.availableMobListGui.bottom = this.height - 30;
        this.availableMobListGui.right = this.width - 25;


        this.availableMobListGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format(status), this.width / 2, 16, 16777215);
        drawFooter(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawFooter(int p_146278_1_)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, ((float)this.height / f + (float)p_146278_1_));
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, ((float)this.width / f), ((float)this.height / f + (float)p_146278_1_));
        //tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, ((float)this.width / f), p_146278_1_);
        //tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, p_146278_1_);
        tessellator.addVertexWithUV(this.width, this.height - 30, 0.0D, 0, p_146278_1_);
        tessellator.addVertexWithUV(0.0D, this.height - 30, 0.0D, 0.0D, p_146278_1_);
        tessellator.draw();
    }
}
