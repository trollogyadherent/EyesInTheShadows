package trollogyadherent.eyesintheshadows.configpickers.mob;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public class MobListEntry {
    protected final String mobString;
    protected final Minecraft mc;

    protected final MobSelectionGui previous;
    //private static final ResourceLocation temp = new ResourceLocation("textures/gui/resource_packs.png");
    //private /*static*/ ResourceLocation skinResourceLocation = new ResourceLocation("textures/items/potion_bottle_empty.png");
    public MobListEntry(MobSelectionGui mobSelectionGui, String mobString) {
        this.previous = mobSelectionGui;
        this.mobString = mobString;
        this.mc = Minecraft.getMinecraft();

    }

    public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        /*this.bindIcon();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l2 = 8;
        int i3 = 8;

        Gui.func_152125_a(p_148279_2_, p_148279_3_, 8.0F, (float) l2, 8, i3, 32, 32, 64.0F, 64.0F);

         */




        int i2;
        String s = this.getMobString();
        i2 = this.mc.fontRenderer.getStringWidth(s);

        if (i2 > 157)
        {
            s = this.mc.fontRenderer.trimStringToWidth(s, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }

        this.mc.fontRenderer.drawStringWithShadow(s, p_148279_2_ + 2, p_148279_3_ + 1, 16777215);
        java.util.List list = this.mc.fontRenderer.listFormattedStringToWidth(this.getDescription(), 157);

        for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2)
        {
            this.mc.fontRenderer.drawStringWithShadow((String)list.get(j2), p_148279_2_  + 2, p_148279_3_ + 12 + 10 * j2, 8421504);
        }
    }

    protected String getDescription() {
        return mobString;
    }

    protected String getMobString() {
        return I18n.format(mobString);
    }

    protected boolean func_148310_d()
    {
        return true;
    }

    protected boolean func_148309_e()
    {
        return !this.previous.hasMobListEntry(this);
    }

    protected boolean func_148308_f()
    {
        return this.previous.hasMobListEntry(this);
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
        if (this.func_148310_d() /*&& p_148278_5_ <= 32 */)
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
