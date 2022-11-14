package helpers;

import java.util.HashMap;

public final class UnitProp {
    private UnitProp() {
    }

    static class Proprieties {
        String type;
        UnitPos position;
        boolean tank;

        Proprieties(final String type, final UnitPos position, final boolean tank) {
            this.type = type;
            this.position = position;
            this.tank = tank;
        }
    }

    private static final HashMap<String, Proprieties> UNIT_PROPS = new HashMap<>() {
        @Override
        public Proprieties get(final Object key) {
            if (!containsKey(key)) {
                return new Proprieties("none", null, false);
            }
            return super.get(key);
        }
    };

    static {
        // Standard Cards//
        UNIT_PROPS.put("Sentinel", new Proprieties("Standard", UnitPos.RANGED, false));
        UNIT_PROPS.put("Berserker", new Proprieties("Standard", UnitPos.RANGED, false));
        UNIT_PROPS.put("Goliath", new Proprieties("Standard", UnitPos.CLOSE, true));
        UNIT_PROPS.put("Warden", new Proprieties("Standard", UnitPos.CLOSE, true));
        // Special Cards //
        UNIT_PROPS.put("The Ripper", new Proprieties("Special", UnitPos.CLOSE, false));
        UNIT_PROPS.put("Miraj", new Proprieties("Special", UnitPos.CLOSE, false));
        UNIT_PROPS.put("The Cursed One", new Proprieties("Special", UnitPos.RANGED, false));
        UNIT_PROPS.put("Disciple", new Proprieties("Special", UnitPos.RANGED, false));
        // Environment Cards //
        UNIT_PROPS.put("Firestorm", new Proprieties("Environment", null, false));
        UNIT_PROPS.put("Winterfell", new Proprieties("Environment", null, false));
        UNIT_PROPS.put("Heart Hound", new Proprieties("Environment", null, false));
    }

    /**
     * Checks if a card is tank.
     * It is used at Card creation.
     * @param name Name of Card.
     * @return True if unit is tank, false otherwise.
     */
    public static boolean isTank(final String name) {
        return UNIT_PROPS.get(name).tank;
    }

    /**
     * Return table position of a card.
     * It is used at Card creation.
     * @param name Name of Card.
     * @return Table Position.
     */
    public static UnitPos getPosition(final String name) {
        return UNIT_PROPS.get(name).position;
    }

    /**
     * Return unit type of card.
     * It is used at Card creation.
     * @param name Name of Card.
     * @return Unit Type.
     */
    public static String getType(final String name) {
        return UNIT_PROPS.get(name).type;
    }
}
