package trollogyadherent.eyesintheshadows.gui.configpickers.potion;

import cpw.mods.fml.client.config.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

/* This is the gui with the list (contains the potion buttons, and + - buttons) */

public class GuiEditArrayPotionID extends GuiEditArray {
    public Object[] beforeValuesReflected;
    public Object[] currentValuesReflected;
    Method saveListChangesMethod;
    public GuiConfig parentGuiConfigScreen;
    public Object[] thisCurrentValues;

    public GuiEditArrayPotionID(GuiConfig parentScreen, IConfigElement configElement, int slotIndex, Object[] currentValues, boolean enabled) {
        super(parentScreen, configElement, slotIndex, currentValues, enabled);
        parentGuiConfigScreen = parentScreen;
        thisCurrentValues = currentValues;
        EyesInTheShadows.debug("Constructing GuiEditArrayPotionID");
    }

    public static class ReturnInfo {
        public GuiConfig parentScreen;
        public IConfigElement configElement;
        public int slotIndex;
        public boolean enabled;
        public Object[] values;

        public ReturnInfo(GuiConfig parentScreen, IConfigElement configElement, int slotIndex, boolean enabled, Object[] values) {
            this.parentScreen = parentScreen;
            this.configElement = configElement;
            this.slotIndex = slotIndex;
            this.enabled = enabled;
            this.values = values;
        }
    }

    @Override
    public void initGui() {
        try {
            beforeValuesReflected = (Object[]) EyesInTheShadows.varInstanceClient.beforeValuesField.get(this);
            currentValuesReflected = (Object[]) EyesInTheShadows.varInstanceClient.currentValuesField.get(this);

            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            //super.entryList = new GuiEditArrayEntriesPotionId(this, this.mc, this.configElement, beforeValuesReflected, currentValuesReflected);

            /* Adding the widget containing the potion list items */
            EyesInTheShadows.varInstanceClient.entryListField.set(this, new GuiEditArrayEntriesPotionId(parentScreen, this, this.mc, this.configElement, beforeValuesReflected, currentValuesReflected, new ReturnInfo(parentGuiConfigScreen, configElement, slotIndex, enabled, thisCurrentValues)));
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect");
            e.printStackTrace();
            return;
        }

        int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
        int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
        int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
        int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
        //this.buttonList.add(btnDone = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
        //this.buttonList.add(btnDefault = new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
        //this.buttonList.add(btnUndoChanges = new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));

        try {
            EyesInTheShadows.varInstanceClient.btnDoneField.set(this, new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
            this.buttonList.add(EyesInTheShadows.varInstanceClient.btnDoneField.get(this));

            EyesInTheShadows.varInstanceClient.btnDefaultField.set(this, new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
            this.buttonList.add(EyesInTheShadows.varInstanceClient.btnDefaultField.get(this));

            EyesInTheShadows.varInstanceClient.btnUndoChangesField.set(this, new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
            this.buttonList.add(EyesInTheShadows.varInstanceClient.btnUndoChangesField.get(this));
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect");
            e.printStackTrace();
            return;
        }

        try {
            EyesInTheShadows.debug("entries:");
            GuiEditArrayEntries entryList = (GuiEditArrayEntries)EyesInTheShadows.varInstanceClient.entryListField.get(this);
            if (entryList == null) {
                EyesInTheShadows.debug("entryList null");
                return;
            }
            if (entryList.listEntries == null) {
                EyesInTheShadows.debug("entryList.listEntries null");
                return;
            }
            for (GuiEditArrayEntries.IArrayEntry iArrayEntry : entryList.listEntries) {
                GuiEditArrayEntries.BaseEntry baseEntry = (GuiEditArrayEntries.BaseEntry) iArrayEntry;
                EyesInTheShadows.debug(String.valueOf(baseEntry.getValue()));
            }
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect fields! (3) (GuiEditArrayPotionID)");
            e.printStackTrace();
        }
    }

    public void actionResetToDefault() {
        EyesInTheShadows.debug("actionResetToDefault called");
        try {
            //this.currentValues = configElement.getDefaults();
            EyesInTheShadows.varInstanceClient.currentValuesField.set(this, configElement.getDefaults());
            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            Object[] beforeValues = (Object[])EyesInTheShadows.varInstanceClient.beforeValuesField.get(this);
            Object[] currentValues = (Object[])EyesInTheShadows.varInstanceClient.currentValuesField.get(this);
            EyesInTheShadows.varInstanceClient.entryListField.set(this, new GuiEditArrayEntriesPotionId(parentScreen, this, this.mc, this.configElement, beforeValues, currentValues, new ReturnInfo(parentGuiConfigScreen, configElement, slotIndex, enabled, thisCurrentValues)));
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect fields! (1) (GuiEditArrayPotionID)");
            e.printStackTrace();
        }
    }

    public void actionSave() {
        EyesInTheShadows.debug("actionSave called");
        try {
            //this.entryList.saveListChanges();
            GuiEditArrayEntries entryList = (GuiEditArrayEntries)EyesInTheShadows.varInstanceClient.entryListField.get(this);
            saveListChangesMethod  = ReflectionHelper.findMethod(cpw.mods.fml.client.config.GuiEditArrayEntries.class, entryList, new String[]{"saveListChanges"});
            saveListChangesMethod.invoke(entryList);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void actionUndoChanges() {
        EyesInTheShadows.debug("actionUndoChanges called");
        try {
            //this.currentValues = Arrays.copyOf(beforeValues, beforeValues.length);
            Object[] beforeValues = (Object[]) EyesInTheShadows.varInstanceClient.beforeValuesField.get(this);
            EyesInTheShadows.varInstanceClient.currentValuesField.set(this, Arrays.copyOf(beforeValues, beforeValues.length));
            //this.entryList = new GuiEditArrayEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
            Object[] currentValues = (Object[])EyesInTheShadows.varInstanceClient.currentValuesField.get(this);
            EyesInTheShadows.varInstanceClient.entryListField.set(this, new GuiEditArrayEntries(this, this.mc, this.configElement, beforeValues, currentValues));
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect fields! (2) (GuiEditArrayPotionID)");
            e.printStackTrace();
        }
    }

    public void actionDisplayPreviousScreen() {
        EyesInTheShadows.debug("actionDisplayPreviousScreen called");
        this.mc.displayGuiScreen(this.parentScreen);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 2000) {
            EyesInTheShadows.debug("Pressed button ID 2000");
            actionSave();
            actionDisplayPreviousScreen();
        } else if (button.id == 2001) {
            EyesInTheShadows.debug("Pressed button ID 2001");
            actionResetToDefault();
        } else if (button.id == 2002) {
            EyesInTheShadows.debug("Pressed button ID 2002");
            actionUndoChanges();
        }
    }
}
