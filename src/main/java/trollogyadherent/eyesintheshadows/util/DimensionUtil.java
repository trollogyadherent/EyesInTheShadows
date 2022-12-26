package trollogyadherent.eyesintheshadows.util;

import net.minecraft.world.WorldProvider;
import trollogyadherent.configmaxxing.ConfigMaxxing;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

public class DimensionUtil {
    public static class SimpleDimensionObj {
        private final int id;
        private final String name;
        private final String leaveMessage;

        public SimpleDimensionObj(int id, String name, String leaveMessage) {
            this.id = id;
            this.name = name;
            this.leaveMessage = leaveMessage;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLeaveMessage() {
            return leaveMessage;
        }
    }
    public static void test() {
        System.out.println("testerino dimensions");

            for (int i : EyesInTheShadows.varInstanceCommon.providers.keySet()) {
            WorldProvider worldProvider = null;
            try {
                if (EyesInTheShadows.varInstanceCommon.providers.get(i) != null) {
                    worldProvider = EyesInTheShadows.varInstanceCommon.providers.get(i).newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (worldProvider == null) {
                continue;
            }
            System.out.println(worldProvider.dimensionId + ", " + worldProvider.getDimensionName() + ", " + worldProvider.getDepartMessage());
        }
    }

    public static SimpleDimensionObj getSimpleDimensionObj(String name) {
        if (EyesInTheShadows.varInstanceCommon.providers == null) {
            return null;
        }
        for (int i : EyesInTheShadows.varInstanceCommon.providers.keySet()) {
            WorldProvider worldProvider = null;
            try {
                if (EyesInTheShadows.varInstanceCommon.providers.get(i) != null) {
                    worldProvider = EyesInTheShadows.varInstanceCommon.providers.get(i).newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (worldProvider == null) {
                continue;
            }
            if (name.equals(worldProvider.getDimensionName())) {
                return new SimpleDimensionObj(worldProvider.dimensionId, worldProvider.getDimensionName(), worldProvider.getDepartMessage());
            }
        }
        return null;
    }

    public static SimpleDimensionObj getSimpleDimensionObj(int id) {
        if (EyesInTheShadows.varInstanceCommon.providers == null) {
            return null;
        }
        for (int i : EyesInTheShadows.varInstanceCommon.providers.keySet()) {
            WorldProvider worldProvider = null;
            try {
                if (EyesInTheShadows.varInstanceCommon.providers.get(i) != null) {
                    worldProvider = EyesInTheShadows.varInstanceCommon.providers.get(i).newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (worldProvider == null) {
                continue;
            }
            if (worldProvider.dimensionId == id) {
                return new SimpleDimensionObj(worldProvider.dimensionId, worldProvider.getDimensionName(), worldProvider.getDepartMessage());
            }
        }
        return null;
    }
}
