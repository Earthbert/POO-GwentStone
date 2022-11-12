package utils;

import java.util.HashMap;

public class UnitProp {
    static class Proprieties {
        String type;
        UnitPos position;
        boolean tank;

        public Proprieties(String type, UnitPos position, boolean tank) {
            this.type = type;
            this.position = position;
            this.tank = tank;
        }
    }

    private static final HashMap<String, Proprieties> unitProps = new HashMap<>() {
        @Override
        public Proprieties get(Object key) {
            if(! containsKey(key))
                return new Proprieties("none", null, false);
            return super.get(key);
        }
    };

    static {
        // Standard Cards//
        unitProps.put("Sentinel", new Proprieties("Standard", UnitPos.RANGED, false));
        unitProps.put("Berserker", new Proprieties("Standard", UnitPos.RANGED, false));
        unitProps.put("Goliath", new Proprieties("Standard", UnitPos.CLOSE, true));
        unitProps.put("Warden", new Proprieties("Standard", UnitPos.CLOSE, true));
        // Special Cards //
        unitProps.put("The Ripper", new Proprieties("Special", UnitPos.RANGED, false));
        unitProps.put("Miraj", new Proprieties("Special", UnitPos.RANGED, false));
        unitProps.put("The Cursed One", new Proprieties("Special", UnitPos.RANGED, false));
        unitProps.put("Disciple", new Proprieties("Special", UnitPos.RANGED, false));
        // Environment Cards //
        unitProps.put("Firestorm", new Proprieties("Environment", null, false));
        unitProps.put("Winterfell", new Proprieties("Environment", null, false));
        unitProps.put("Heart Hound", new Proprieties("Environment", null, false));
    }

    public static boolean isTank (String name) {
        return unitProps.get(name).tank;
    }

    public static UnitPos getPosition (String name) {
        return unitProps.get(name).position;
    }

    public static String getType(String name) {
        return unitProps.get(name).type;
    }
}
