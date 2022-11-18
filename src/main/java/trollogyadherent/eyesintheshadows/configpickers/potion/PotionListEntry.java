package trollogyadherent.eyesintheshadows.configpickers.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import org.lwjgl.opengl.GL11;
import trollogyadherent.eyesintheshadows.util.ClientUtil;
import trollogyadherent.eyesintheshadows.util.Util;

public class PotionListEntry {
    protected final Potion potion;
    protected final Minecraft mc;

    protected final PotionSelectionGui previous;
    //private static final ResourceLocation temp = new ResourceLocation("textures/gui/resource_packs.png");
    //private /*static*/ ResourceLocation skinResourceLocation = new ResourceLocation("textures/items/potion_bottle_empty.png");
    public PotionListEntry(PotionSelectionGui potionSelectionGui, Potion potion) {
        this.previous = potionSelectionGui;
        this.potion = potion;
        this.mc = Minecraft.getMinecraft();
    }

    public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        int color  = potion.getLiquidColor();
        int r = (color >> 16)&255;
        int g = (color >> 8)&255;
        int b = (color >> 0)&255;

        float r_ = r / (float)255;
        float g_ = g / (float)255;
        float b_ = b / (float)255;

        GL11.glColor4f(r_, g_, b_, 1);
        ClientUtil.drawModalRectWithCustomSizedTexture(mc, previous.potionOverlayResourceLocation, p_148279_2_, p_148279_3_, 32, 32, 1, 1);

        GL11.glColor4f(1, 1, 1, 1);
        ClientUtil.drawModalRectWithCustomSizedTexture(mc, previous.potionResourceLocation, p_148279_2_, p_148279_3_, 32, 32, 1, 1);

        int i2;
        String s = this.getPotionName();
        i2 = this.mc.fontRenderer.getStringWidth(s);

        if (i2 > 157)
        {
            s = this.mc.fontRenderer.trimStringToWidth(s, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }

        this.mc.fontRenderer.drawStringWithShadow(s, p_148279_2_ + 32 + 2, p_148279_3_ + 1, 16777215);
        java.util.List list = this.mc.fontRenderer.listFormattedStringToWidth(this.getSkinDescription(), 157);

        for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2)
        {
            this.mc.fontRenderer.drawStringWithShadow((String)list.get(j2), p_148279_2_ + 32 + 2, p_148279_3_ + 12 + 10 * j2, 8421504);
        }
    }

    protected String getSkinDescription() {
        return potion.getName() + " - id: " + potion.getId();
    }

    protected String getPotionName() {
        String prefix = Util.colorCode(Util.Color.GREEN);
        if (potion.isBadEffect()) {
            prefix = Util.colorCode(Util.Color.RED);
        }
        return prefix + I18n.format(potion.getName());
    }

    protected void bindIcon() {
        this.mc.getTextureManager().bindTexture(previous.potionResourceLocation); //bindTexturePackIcon(this.field_148317_a.getTextureManager());
    }

    protected boolean showHoverOverlay()
    {
        return true;
    }

    protected boolean func_148309_e()
    {
        return !this.previous.hasPotionListEntry(this);
    }

    protected boolean func_148308_f()
    {
        return this.previous.hasPotionListEntry(this);
    }

    /*protected boolean func_148314_g()
    {
        java.util.List list = this.previous.probablyToRemove(this);
        int i = list.indexOf(this);
        return i > 0 && ((SkinListEntry)list.get(i - 1)).func_148310_d();
    }

    protected boolean func_148307_h()
    {
        List list = this.previous.probablyToRemove(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && ((SkinListEntry)list.get(i + 1)).func_148310_d();
    }*/

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        if (this.showHoverOverlay() /*&& p_148278_5_ <= 32 */)
        {
            return true;
        }

        return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
}
