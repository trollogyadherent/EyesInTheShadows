package trollogyadherent.eyesintheshadows.configpickers.mob;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

import java.util.Arrays;

/* The button that opens the potion array gui */
public class ArrayEntryMobString extends GuiConfigEntries.ArrayEntry {
    public ArrayEntryMobString(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    public void valueButtonPressed(int slotIndex)
    {
        mc.displayGuiScreen(new GuiEditArrayMobString(this.owningScreen, configElement, slotIndex, currentValues, enabled()));
    }

    @Override
    public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        EyesInTheShadows.debug(Arrays.toString(currentValues));
        return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
    }
}
