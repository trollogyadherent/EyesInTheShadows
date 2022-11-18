package trollogyadherent.eyesintheshadows.configpickers.mob;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.configpickers.potion.GuiEditArrayPotionID;
import trollogyadherent.eyesintheshadows.util.ClientUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* The gui presenting the list of potions to choose from */
public class MobSelectionGui extends GuiScreen {
    GuiEditArrayMobString.ReturnInfo returnInfo;
    int index;
    private List availableMobs;
    private String status = I18n.format("eyesintheshadows.mob_selection_title");
    private AvailableMobListGui availableMobListGui;
    String originalValue;
    ResourceLocation mobBackground = new ResourceLocation("textures/gui/demo_background.png");

    public MobSelectionGui(GuiEditArrayMobString parentScreen, Object[] beforeValues, Object[] currentValues, int index, GuiEditArrayMobString.ReturnInfo returnInfo) {


        this.returnInfo = returnInfo;
        this.index = index;
        this.mc = Minecraft.getMinecraft();

        EyesInTheShadows.debug("currentValuesReflected:");
        EyesInTheShadows.debug(Arrays.toString(parentScreen.currentValuesReflected));

        EyesInTheShadows.debug("currentValues passed as args:");
        EyesInTheShadows.debug(Arrays.toString(currentValues));

        this.availableMobs = new ArrayList();
        //this.status = I18n.format("offlineauth.skingui.select_skin");

        System.out.println(returnInfo.values[index]);
        this.originalValue = (String) returnInfo.values[index];

        /*for (Potion p : Potion.potionTypes) {
            if (p != null) {
                MobListEntry entry = new MobListEntry(this, p);
                this.availablePotions.add(entry);
            }
        }*/

        for (Object e : EntityList.stringToClassMapping.keySet()) {
            MobListEntry entry = new MobListEntry(this, (String) e);
            this.availableMobs.add(entry);
        }
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        //list.add(new DummyConfigElement<>("modIDSelector", "FML", ConfigGuiType.MOD_ID, "bruh sneed"));
        return list;
    }

    @Override
    public void initGui() {
        this.availableMobListGui = new AvailableMobListGui(this.mc, 35, this.height - 30, this.width / 2 - 45, this.height, 36, this.availableMobs, returnInfo, index);
        //this.availablePotionListGui.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableMobListGui.setSlotXBoundsFromLeft(this.width / 2);
        //this.availablePotionListGui.setS
        this.availableMobListGui.registerScrollButtons(7, 8);

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
            this.availableMobListGui.moveSelectionDown();
        } else if (eventKey == Keyboard.KEY_UP) {
            this.availableMobListGui.moveSelectionUp();
        } else {
            EyesInTheShadows.debug("Index: " + index);
            EyesInTheShadows.debug("width: " + this.width + ", height: " + this.height);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) {
            showParentScreen();
        }
    }


    public boolean hasMobListEntry(MobListEntry mobListEntry)
    {
        return this.availableMobs.contains(mobListEntry);
    }

    public List probablyToRemove(MobListEntry mobListEntry)
    {
        return this.hasMobListEntry(mobListEntry) ? this.availableMobs : null;
    }

    public List getAvailableMobs()
    {
        return this.availableMobs;
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

        //this.availableMobListGui.top = 35;
        this.availableMobListGui.left = 25;
        //this.availableMobListGui.bottom = this.height - 30;
        this.availableMobListGui.right = this.width / 2;


        this.availableMobListGui.drawScreen(mouseX, mouseY, partialTicks);
        drawMobBackground(this.width / 2 + 15, 60, this.width / 2 - 70, this.height - 90);
        this.drawCenteredString(this.fontRendererObj, I18n.format(status), this.width / 2, 16, 16777215);
        drawFooter();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawFooter()
    {
        float opaqueColor = 0.25098039215F;
        GL11.glColor4f(opaqueColor, opaqueColor, opaqueColor, 1);
        ClientUtil.drawModalRectWithCustomSizedTexture(mc, optionsBackground, 0, this.height - 30, this.width, 30, this.width / 32, 1);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public void actionCancelValues() {
        returnInfo.values[index] = originalValue;
    }

    public void showParentScreen() {
        mc.displayGuiScreen(new GuiEditArrayMobString(returnInfo.parentScreen, returnInfo.configElement, returnInfo.slotIndex, returnInfo.values, returnInfo.enabled));
    }

    public void drawMobBackground(int x, int y, int drawWidth, int drawHeight) {
        this.mc.getTextureManager().bindTexture(mobBackground);

        GL11.glEnable(GL11.GL_BLEND);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setTranslation(0, 0, 0);
        tessellator.startDrawingQuads();

        /* Ok so basically, the texture is a bit smaller than the size of the image itself.
         * These values are passed to the tesselator and tell it how much of the image it should actually draw,
         * in a 0 to 1 double (like a percentage). */
        double textureWidth = 0.96875;
        double textureHeight = 0.6484375;

        tessellator.addVertexWithUV(x, (y + drawHeight), 0.0D, 0, textureHeight);
        tessellator.addVertexWithUV((x + drawWidth), (y + drawHeight), 0.0D, textureWidth, textureHeight);
        tessellator.addVertexWithUV((x + drawWidth), y, 0.0D, textureWidth, 0);
        tessellator.addVertexWithUV(x, y, 0.0D, 0, 0);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
    }
}
