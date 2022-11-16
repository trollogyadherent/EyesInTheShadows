package trollogyadherent.eyesintheshadows.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.potion.Potion;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

public class PotionUtil {
    public static Potion getPotionById(int id) {
        Potion res = null;
        for (Potion p : Potion.potionTypes) {
            if (p != null && p.getId() == id) {
                res = p;
                EyesInTheShadows.debug("Found potion with id " + id + "! (" + p.getName() + ")");
            }
        }
        return res;
    }

    public static void printPotionIds() {
        EyesInTheShadows.info("=========Potion List=========");
        EyesInTheShadows.info("The printing of this list is for you to know which enchantment has which id. You can disable this print in the configs.");
        for (Potion p : Potion.potionTypes) {
            if (p != null) {
                EyesInTheShadows.info(p.getId() + ": " + p.getName());
            }
        }
        EyesInTheShadows.info("=============================");
    }
}
