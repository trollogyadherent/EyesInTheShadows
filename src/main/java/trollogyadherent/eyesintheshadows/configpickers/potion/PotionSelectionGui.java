package trollogyadherent.eyesintheshadows.configpickers.potion;

import cpw.mods.fml.client.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.GL11;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.util.ClientUtil;

import java.util.*;

/* The gui presenting the list of potions to choose from */
public class PotionSelectionGui extends GuiScreen {
    GuiEditArrayPotionID.ReturnInfo returnInfo;
    int index;
    private List availablePotions;
    private String status = I18n.format("eyesintheshadows.potion_selection_title");
    private AvailablePotionListGui availablePotionListGui;
    int originalValue;
    ResourceLocation potionResourceLocation = new ResourceLocation("textures/items/potion_bottle_empty.png");
    ResourceLocation potionSplashResourceLocation = new ResourceLocation("textures/items/potion_bottle_splash.png");
    ResourceLocation potionOverlayResourceLocation = new ResourceLocation("textures/items/potion_overlay.png");

    int lastKeyboardEventKey = -1;

    public PotionSelectionGui(GuiEditArrayPotionID parentScreen, Object[] beforeValues, Object[] currentValues, int index, GuiEditArrayPotionID.ReturnInfo returnInfo) {


        this.returnInfo = returnInfo;
        this.index = index;
        this.mc = Minecraft.getMinecraft();

        EyesInTheShadows.debug("currentValuesReflected:");
        EyesInTheShadows.debug(Arrays.toString(parentScreen.currentValuesReflected));

        EyesInTheShadows.debug("currentValues passed as args:");
        EyesInTheShadows.debug(Arrays.toString(currentValues));

        this.availablePotions = new ArrayList();
        //this.status = I18n.format("offlineauth.skingui.select_skin");

        if (index < 0 || index >= returnInfo.values.length) {
            this.originalValue = 0;
        } else {
            if (returnInfo.values[index] instanceof String) {
                this.originalValue = Integer.parseInt((String) returnInfo.values[index]);
            } else {
                this.originalValue = (int) returnInfo.values[index];
            }
        }

        for (Potion p : Potion.potionTypes) {
            if (p != null) {
                PotionListEntry entry = new PotionListEntry(this, p);
                this.availablePotions.add(entry);
            }
        }
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        //list.add(new DummyConfigElement<>("modIDSelector", "FML", ConfigGuiType.MOD_ID, "bruh sneed"));
        return list;
    }

    @Override
    public void initGui() {
        this.availablePotionListGui = new AvailablePotionListGui(this.mc, 35, this.height - 30, this.width / 4 * 3, this.height, 36, this.availablePotions, returnInfo, index);
        //this.availablePotionListGui.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availablePotionListGui.setSlotXBoundsFromLeft(this.width / 2);
        //this.availablePotionListGui.setS
        this.availablePotionListGui.registerScrollButtons(7, 8);

        this.buttonList.add(new GuiButton(3, 20, this.height - 25, (this.width - 25) / 4, 20, I18n.format("gui.done")));
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            EyesInTheShadows.debug("hit escape");
            actionCancelValues();
            showParentScreen();
        } else if (eventKey == Keyboard.KEY_RETURN) {
            showParentScreen();
        } else if (eventKey == Keyboard.KEY_DOWN) {
            this.availablePotionListGui.moveSelectionDown();
        } else if (eventKey == Keyboard.KEY_UP) {
            this.availablePotionListGui.moveSelectionUp();
        } else {
            EyesInTheShadows.debug("Index: " + index);
            EyesInTheShadows.debug("width: " + this.width + ", height: " + this.height);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 3)
        {
            showParentScreen();
        }
    }


    public boolean hasPotionListEntry(PotionListEntry potionListEntry)
    {
        return this.availablePotions.contains(potionListEntry);
    }

    public List probablyToRemove(PotionListEntry skinListEntry)
    {
        return this.hasPotionListEntry(skinListEntry) ? this.availablePotions : null;
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
        this.availablePotionListGui.func_148179_a(par1, par2, par3);
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

        //this.availablePotionListGui.top = 35;
        this.availablePotionListGui.left = 25;
        //this.availablePotionListGui.bottom = this.height - 30;
        this.availablePotionListGui.right = this.width - 25;

        this.availablePotionListGui.setListWidth(this.width / 4 * 3);
        //this.availablePotionListGui.scrollBy(this.availablePotionListGui.selectedIndex);

        this.availablePotionListGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format(status), this.width / 2, 16, 16777215);
        drawFooter();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawFooter()
    {
        /*GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);

        float top = this.height - 30;

        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, ((float)this.height / f));
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, ((float)this.width / f), ((float)this.height / f));

        //tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, ((float)this.width / f), p_146278_1_);
        //tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, p_146278_1_);

        tessellator.addVertexWithUV(this.width, this.height - 30, 0.0D, ((float)this.width / f), top);
        tessellator.addVertexWithUV(0.0D, this.height - 30, 0.0D, 0.0D, top);
        tessellator.draw();*/

        //Tessellator.instance.setColorOpaque_I(4210752);
        float opaqueColor = 0.25098039215F;
        GL11.glColor4f(opaqueColor, opaqueColor, opaqueColor, 1);
        ClientUtil.drawModalRectWithCustomSizedTexture(mc, optionsBackground, 0, this.height - 30, this.width, 30, this.width / 32, 1);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public void keyReleased(char eventChar, int eventKey) {

    }

    public void keyPressedOnce(char eventChar, int eventKey) {

    }

    @Override
    public void handleKeyboardInput() {
        char eventChar = Keyboard.getEventCharacter();
        int eventKey =  Keyboard.getEventKey();
        /* True means it is down, false means it's released */
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(eventChar, eventKey);
            if (this.lastKeyboardEventKey != eventKey) {
                this.keyPressedOnce(eventChar, eventKey);
                this.lastKeyboardEventKey = eventKey;
            }
        } else {
            this.keyReleased(eventChar, eventKey);
            this.lastKeyboardEventKey = -1;
        }

        this.mc.func_152348_aa();
    }

    public void actionCancelValues() {
        returnInfo.values[index] = originalValue;
    }

    public void showParentScreen() {
        mc.displayGuiScreen(new GuiEditArrayPotionID(returnInfo.parentScreen, returnInfo.configElement, returnInfo.slotIndex, returnInfo.values, returnInfo.enabled));
    }
}
