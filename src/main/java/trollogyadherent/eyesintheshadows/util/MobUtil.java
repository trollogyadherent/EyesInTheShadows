package trollogyadherent.eyesintheshadows.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class MobUtil {
    public static void test() {
        for (Object e : EntityList.stringToClassMapping.keySet()) {
            String name = (String) e;
            System.out.println(name);
            //System.out.println(EntityList.stringToClassMapping.get(name));
            //System.out.println(EntityList.classToStringMapping.get(e));
        }
    }
}
