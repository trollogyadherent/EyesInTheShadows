package trollogyadherent.eyesintheshadows.gui.configpickers.potion;

import cpw.mods.fml.client.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.util.PotionUtil;
import trollogyadherent.eyesintheshadows.util.Util;

import java.util.ArrayList;
import java.util.Arrays;

/* The widget containing the potion list entries. it contains the + - buttons and the potion buttons. */
public class GuiEditArrayEntriesPotionId extends GuiEditArrayEntries {
    static GuiEditArrayPotionID owningGuiReflected;
    public GuiEditArray parent;
    public GuiScreen parentScreen;
    public static GuiEditArrayPotionID.ReturnInfo returnInfo;
    public static GuiEditArrayEntriesPotionId this_;

    public GuiEditArrayEntriesPotionId(GuiScreen parentScreen, GuiEditArrayPotionID parent, Minecraft mc, IConfigElement configElement, Object[] beforeValues, Object[] currentValues, GuiEditArrayPotionID.ReturnInfo returnInfo) {
        super(parent, mc, configElement, beforeValues, currentValues);
        EyesInTheShadows.debug("Constructing GuiEditArrayEntriesPotionId");
        this.parentScreen = parentScreen;
        this.parent = parent;

        this.listEntries = new ArrayList<>();
        this.returnInfo = returnInfo;
        this_ = this;

        try {
            owningGuiReflected = (GuiEditArrayPotionID) EyesInTheShadows.varInstanceClient.owningGuiField.get(this);
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect");
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < currentValues.length; i++) {
            listEntries.add(new PotionEntry(parentScreen, owningGuiReflected, this, configElement, Integer.parseInt(currentValues[i].toString()), i));
        }
        //for (Object value : currentValues)

        listEntries.add(new BaseEntryPotionId(parentScreen, owningGuiReflected, this, configElement));
    }

    @Override
    public void addNewEntry(int index)
    {
        PotionEntry potionEntry = new PotionEntry(parentScreen, owningGuiReflected, this, this.configElement, 0, currentValues.length);

        //Object[] newArr = new Object[currentValues.length + 1];
        //System.arraycopy(currentValues, 0, newArr, 0, currentValues.length);
        //newArr[currentValues.length] = 0;

        Object[] newArr = Util.addAtIndex(currentValues, index, 0);

        currentValues = newArr;
        returnInfo.values = newArr;
        listEntries.add(index, potionEntry);
        this.canAddMoreEntries = !configElement.isListLengthFixed()
                && (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }

    @Override
    public void removeEntry(int index)
    {
        Object[] newArr = Util.removeAtIndex(currentValues, index);
        currentValues = newArr;
        returnInfo.values = newArr;
        super.removeEntry(index);
    }

    public static class BaseEntryPotionId extends BaseEntry {
        GuiEditArrayEntriesPotionId owningEntriesPotion;

        public BaseEntryPotionId(GuiScreen parentScreen, GuiEditArray owningScreen, GuiEditArrayEntriesPotionId owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
            owningEntriesPotion = owningEntryList;
        }

        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnAddNewEntryAbove.mousePressed(owningEntryList.mc, x, y))
            {
                EyesInTheShadows.debug("adding new entry!");
                EyesInTheShadows.debug(Arrays.toString(owningEntryList.currentValues));
                btnAddNewEntryAbove.func_146113_a(owningEntryList.mc.getSoundHandler());
                (owningEntryList).addNewEntry(index);
                owningEntryList.recalculateState();
                EyesInTheShadows.debug("after adding:");
                EyesInTheShadows.debug(Arrays.toString(owningEntryList.currentValues));
                owningGuiReflected.mc.displayGuiScreen(new PotionSelectionGui(owningGuiReflected, owningGuiReflected.beforeValuesReflected, owningGuiReflected.currentValuesReflected, index, returnInfo));
                return true;
            }
            else if (this.btnRemoveEntry.mousePressed(owningEntryList.mc, x, y))
            {
                EyesInTheShadows.debug("removing entry!");
                btnRemoveEntry.func_146113_a(owningEntryList.mc.getSoundHandler());
                owningEntryList.removeEntry(index);
                owningEntryList.recalculateState();
                return true;
            }

            return false;
        }
    }

    public static class GuiButtonExtPotion extends GuiButtonExt {
        GuiEditArrayPotionID guiEditArrayPotionIDScreen;
        GuiEditArrayEntriesPotionId owningEntryList;
        int index;
        public GuiButtonExtPotion(int id, int xPos, int yPos, int width, int height, String displayString, GuiEditArrayPotionID guiEditArrayPotionIDScreen, GuiEditArrayEntriesPotionId owningEntryList, int index) {
            super(id, xPos, yPos, width, height, displayString);
            this.guiEditArrayPotionIDScreen = guiEditArrayPotionIDScreen;
            this.owningEntryList = owningEntryList;
            this.index = index;
        }

        @Override
        public void mouseReleased(int x, int y) {
            if (x >= this.xPosition && x <= this.xPosition + this.width && y >= this.yPosition && y <= this.yPosition + this.height) {
                EyesInTheShadows.debug("Clicked me!");

                EyesInTheShadows.debug("currentValues before saving:");
                EyesInTheShadows.debug(Arrays.toString(owningGuiReflected.currentValuesReflected));
                owningGuiReflected.actionSave();
                guiEditArrayPotionIDScreen.mc.displayGuiScreen(new PotionSelectionGui(guiEditArrayPotionIDScreen, owningGuiReflected.beforeValuesReflected, owningGuiReflected.currentValuesReflected, index, returnInfo));
            }
        }
    }

    public static class PotionEntry extends BaseEntryPotionId {
        protected final GuiTextField textFieldValue;
        Potion potion;
        GuiButtonExt btn;
        String displayValue;
        int index;
        public PotionEntry(GuiScreen parentScreen, GuiEditArrayPotionID owningScreen, GuiEditArrayEntriesPotionId owningEntryList, IConfigElement configElement, Object value, int index) {
            super(parentScreen, owningScreen, owningEntryList, configElement);

            this.index = index;

            potion = PotionUtil.getPotionById(Integer.parseInt(value.toString()));
            if (potion == null) {
                displayValue = "None";
            } else {
                displayValue = value + ": " + I18n.format(potion.getName());
            }

            this.btn = new GuiButtonExtPotion(69, owningEntryList.width / 4 + 10, 0, owningEntryList.width / 3 + 1, 18, displayValue, owningScreen, owningEntryList, index);
            this.textFieldValue = new GuiTextField(owningEntryList.mc.fontRenderer, owningEntryList.width / 4 + 1, 0, owningEntryList.controlWidth - 3, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(value.toString());
            this.isValidated = configElement.getValidationPattern() != null;

            if (configElement.getValidationPattern() != null)
            {
                if (configElement.getValidationPattern().matcher(this.textFieldValue.getText().trim()).matches())
                    isValidValue = true;
                else
                    isValidValue = false;
            }

        }


        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            if (configElement.isListLengthFixed() || slotIndex != owningEntryList.listEntries.size() - 1)
            {
                this.textFieldValue.setVisible(true);
                this.textFieldValue.yPosition = y + 1;
                //this.textFieldValue.drawTextBox();
                this.btn.visible = true;
                this.btn.yPosition = y;
                this.btn.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            }
            else
                this.textFieldValue.setVisible(false);
        }

        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            boolean enabledReflected;
            try {
                enabledReflected = (boolean) EyesInTheShadows.varInstanceClient.enabledField.get(owningScreen);
            } catch (IllegalAccessException e) {
                EyesInTheShadows.error("Failed to reflect");
                e.printStackTrace();
                return;
            }
            if (enabledReflected || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
                    || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
            {
                this.textFieldValue.textboxKeyTyped((enabledReflected ? eventChar : Keyboard.CHAR_NONE), eventKey);

                if (configElement.getValidationPattern() != null)
                {
                    if (configElement.getValidationPattern().matcher(this.textFieldValue.getText().trim()).matches())
                        isValidValue = true;
                    else
                        isValidValue = false;
                }
            }
        }

        @Override
        public void updateCursorCounter()
        {
            this.textFieldValue.updateCursorCounter();
        }

        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {
            this.textFieldValue.mouseClicked(x, y, mouseEvent);
            this.btn.mouseReleased(x, y);
        }



        @Override
        public Object getValue()
        {
            return this.textFieldValue.getText().trim();
        }
    }
}
