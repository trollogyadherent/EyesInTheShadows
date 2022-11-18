package trollogyadherent.eyesintheshadows.configpickers.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class AvailablePotionListGui extends GuiListExtended {
    protected final Minecraft mc;
    protected final List potionEntries;

    int selectedIndex;

    int configListIndex;
    GuiEditArrayPotionID.ReturnInfo returnInfo;

    public AvailablePotionListGui(Minecraft mc, int top, int bottom, int listWidth, int listHeight, int entryHeight, List potionEntries, GuiEditArrayPotionID.ReturnInfo returnInfo, int index) {
        super(mc, listWidth, listHeight, top, bottom, entryHeight);
        this.mc = mc;
        this.potionEntries = potionEntries;
        this.returnInfo = returnInfo;
        this.configListIndex = index;
        this.field_148163_i = false;
        this.top = top;
        this.bottom = bottom;
        //this.setHasListHeader(true, (int)((float)mc.fontRenderer.FONT_HEIGHT * 1.5F));
        this.setHasListHeader(false, 0);
        selectedIndex = -1;
        for (int i = 0; i < potionEntries.size(); i++) {
            int intVal;
            if (returnInfo.values[configListIndex] instanceof String) {
                intVal = Integer.parseInt((String) returnInfo.values[configListIndex]);
            } else {
                intVal = (int)returnInfo.values[configListIndex];
            }
            if (((PotionListEntry) potionEntries.get(i)).potion.getId() == intVal) {
                //selectedIndex = i;
                selectEntry(i, true);
                System.out.println("found index+ " + i);
            }
        }
    }

    public void moveSelectionUp() {
        selectEntry(selectedIndex - 1, true);
    }

    public void moveSelectionDown() {
        selectEntry(selectedIndex + 1, true);
    }

    public void selectEntry(int index, boolean doScroll) {
        if (index < 0 || index >= getSize()) {
            return;
        }
        if (doScroll) {
            scrollTo(index);
        }
        selectedIndex = index;
        returnInfo.values[configListIndex] = this.getListEntry_(index).potion.getId();
    }

    public void scrollTo(int index) {
        int yPos = (index + 1) * slotHeight - getAmountScrolled();
        int h = bottom - top;
        scrollBy(yPos - h / 2);
    }

    protected void drawListHeader(int par1, int par2, Tessellator tessellator) {
        //this.setSlotXBoundsFromLeft(10);

        String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + this.getListHeader() + " sneederino";
        this.mc.fontRenderer.drawString(s, par1 + this.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, Math.min(this.top + 3, par2), 16777215);
    }

    protected String getListHeader() {
        return I18n.format("offlineauth.skingui.available_skins");
    }
    public List func_148201_l()
    {
        return this.potionEntries;
    }

    protected int getSize()
    {
        return this.func_148201_l().size();
    }

    public PotionListEntry getListEntry_(int index) {
        return (PotionListEntry) this.func_148201_l().get(index);
    }

    public int getListWidth()
    {
        return this.width;
    }

    public void setListWidth(int width) {
        this.width = width;
    }

    protected int getScrollBarX()
    {
        return this.right - 6;
    }

    @Override
    public IGuiListEntry getListEntry(int p_148180_1_) {
        return null;
    }

    public boolean isSelected(int index) {
        return index == selectedIndex;
    }

    @Override
    protected void drawSlot(int index, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
    {
        this.getListEntry_(index).drawEntry(index, p_148126_2_, p_148126_3_, this.getListWidth(), p_148126_4_, p_148126_5_, p_148126_6_, p_148126_7_, this.func_148124_c(p_148126_6_, p_148126_7_) == index);
    }

    @Override
    public boolean func_148179_a(int p_148179_1_, int p_148179_2_, int p_148179_3_)
    {
        if (this.func_148141_e(p_148179_2_))
        {
            int l = this.func_148124_c(p_148179_1_, p_148179_2_);

            if (l >= 0)
            {
                int i1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                int j1 = this.top + 4 - this.getAmountScrolled() + l * this.slotHeight + this.headerPadding;
                int k1 = p_148179_1_ - i1;
                int l1 = p_148179_2_ - j1;

                if (this.getListEntry_(l).mousePressed(l, p_148179_1_, p_148179_2_, p_148179_3_, k1, l1))
                {
                    selectEntry(l, false);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean func_148181_b(int x, int y, int mouseEvent)
    {
        for (int l = 0; l < this.getSize(); ++l)
        {
            int i1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int j1 = this.top + 4 - this.getAmountScrolled() + l * this.slotHeight + this.headerPadding;
            int k1 = x - i1;
            int l1 = y - j1;
            this.getListEntry_(l).mouseReleased(l, x, y, mouseEvent, k1, l1);
        }

        this.func_148143_b(true);
        return false;
    }
}