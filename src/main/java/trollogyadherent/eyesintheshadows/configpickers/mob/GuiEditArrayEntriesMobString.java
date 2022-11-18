package trollogyadherent.eyesintheshadows.configpickers.mob;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

import java.util.ArrayList;
import java.util.Arrays;

/* The widget containing the potion list entries. it contains the + - buttons and the potion buttons. */
public class GuiEditArrayEntriesMobString extends GuiEditArrayEntries {
    static GuiEditArrayMobString owningGuiReflected;
    public GuiEditArray parent;
    public GuiScreen parentScreen;
    public static GuiEditArrayMobString.ReturnInfo returnInfo;
    public static GuiEditArrayEntriesMobString this_;

    public GuiEditArrayEntriesMobString(GuiScreen parentScreen, GuiEditArrayMobString parent, Minecraft mc, IConfigElement configElement, Object[] beforeValues, Object[] currentValues, GuiEditArrayMobString.ReturnInfo returnInfo) {
        super(parent, mc, configElement, beforeValues, currentValues);
        EyesInTheShadows.debug("Constructing GuiEditArrayEntriesPotionId");
        this.parentScreen = parentScreen;
        this.parent = parent;

        this.listEntries = new ArrayList<>();
        this.returnInfo = returnInfo;
        this_ = this;

        try {
            owningGuiReflected = (GuiEditArrayMobString) EyesInTheShadows.varInstanceClient.owningGuiField.get(this);
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect");
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < currentValues.length; i++) {
            listEntries.add(new MobEntry(parentScreen, owningGuiReflected, this, configElement, currentValues[i], i));
        }
        //for (Object value : currentValues)

        listEntries.add(new BaseEntryMobString(parentScreen, owningGuiReflected, this, configElement));
    }

    @Override
    public void addNewEntry(int index)
    {
        MobEntry mobEntry = new MobEntry(parentScreen, owningGuiReflected, this, this.configElement, "None", currentValues.length);
        Object[] newArr = new Object[currentValues.length + 1];
        System.arraycopy(currentValues, 0, newArr, 0, currentValues.length);
        newArr[currentValues.length] = "None";
        currentValues = newArr;
        returnInfo.values = newArr;
        listEntries.add(index, mobEntry);
        this.canAddMoreEntries = !configElement.isListLengthFixed()
                && (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }

    public static class BaseEntryMobString extends BaseEntry {
        GuiEditArrayEntriesMobString owningEntriesMob;

        public BaseEntryMobString(GuiScreen parentScreen, GuiEditArray owningScreen, GuiEditArrayEntriesMobString owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
            owningEntriesMob = owningEntryList;
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
                owningGuiReflected.mc.displayGuiScreen(new MobSelectionGui(owningGuiReflected, owningGuiReflected.beforeValuesReflected, owningGuiReflected.currentValuesReflected, index, returnInfo));
                return true;
            }
            else if (this.btnRemoveEntry.mousePressed(owningEntryList.mc, x, y))
            {
                btnRemoveEntry.func_146113_a(owningEntryList.mc.getSoundHandler());
                owningEntryList.removeEntry(index);
                owningEntryList.recalculateState();
                return true;
            }

            return false;
        }
    }

    public static class GuiButtonExtMob extends GuiButtonExt {
        GuiEditArrayMobString guiEditArrayMobStringScreen;
        GuiEditArrayEntriesMobString owningEntryList;
        int index;
        public GuiButtonExtMob(int id, int xPos, int yPos, int width, int height, String displayString, GuiEditArrayMobString guiEditArrayMobStringScreen, GuiEditArrayEntriesMobString owningEntryList, int index) {
            super(id, xPos, yPos, width, height, displayString);
            this.guiEditArrayMobStringScreen = guiEditArrayMobStringScreen;
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
                guiEditArrayMobStringScreen.mc.displayGuiScreen(new MobSelectionGui(guiEditArrayMobStringScreen, owningGuiReflected.beforeValuesReflected, owningGuiReflected.currentValuesReflected, index, returnInfo));
            }
        }
    }

    public static class MobEntry extends BaseEntryMobString {
        protected final GuiTextField textFieldValue;
        GuiButtonExt btn;
        String displayValue;
        int index;
        public MobEntry(GuiScreen parentScreen, GuiEditArrayMobString owningScreen, GuiEditArrayEntriesMobString owningEntryList, IConfigElement configElement, Object value, int index) {
            super(parentScreen, owningScreen, owningEntryList, configElement);

            this.index = index;

            displayValue = String.valueOf(value);

            this.btn = new GuiButtonExtMob(69, owningEntryList.width / 4 + 10, 0, owningEntryList.width / 3 + 1, 18, displayValue, owningScreen, owningEntryList, index);
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
