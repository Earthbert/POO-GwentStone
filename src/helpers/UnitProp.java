package helpers;

import java.util.HashMap;
import static helpers.UnitType.*;
import static helpers.UnitPos.*;

public final class UnitProp {
    private UnitProp() {
    }

    static class Proprieties {
        UnitType type;
        UnitPos position;
        boolean tank;
        boolean friendly;

        Proprieties(final UnitType type, final UnitPos position,
                    final boolean tank, final boolean friendly) {
            this.type = type;
            this.position = position;
            this.tank = tank;
            this.friendly = friendly;
        }
    }

    private static final HashMap<String, Proprieties> UNIT_PROPS = new HashMap<>();

    static {
        // Standard Cards//
        UNIT_PROPS.put("Sentinel", new Proprieties(STANDARD, RANGED, false, false));
        UNIT_PROPS.put("Berserker", new Proprieties(STANDARD, RANGED, false, false));
        UNIT_PROPS.put("Goliath", new Proprieties(STANDARD, CLOSE, true, false));
        UNIT_PROPS.put("Warden", new Proprieties(STANDARD, CLOSE, true, false));
        // Special Cards //
        UNIT_PROPS.put("The Ripper", new Proprieties(SPECIAL, CLOSE, false, false));
        UNIT_PROPS.put("Miraj", new Proprieties(SPECIAL, CLOSE, false, false));
        UNIT_PROPS.put("The Cursed One", new Proprieties(SPECIAL, RANGED, false, false));
        UNIT_PROPS.put("Disciple", new Proprieties(SPECIAL, RANGED, false, true));
        // Environment Cards //
        UNIT_PROPS.put("Firestorm", new Proprieties(ENVIRONMENT, null, false, false));
        UNIT_PROPS.put("Winterfell", new Proprieties(ENVIRONMENT, null, false, false));
        UNIT_PROPS.put("Heart Hound", new Proprieties(ENVIRONMENT, null, false, false));
        // Hero Cards //
        UNIT_PROPS.put("Empress Thorina", new Proprieties(HERO, null, false, false));
        UNIT_PROPS.put("General Kocioraw", new Proprieties(HERO, null, false, true));
        UNIT_PROPS.put("King Mudface", new Proprieties(HERO, null, false, true));
        UNIT_PROPS.put("Lord Royce", new Proprieties(HERO, null, false, false));
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
     *
     * @param name Name of Card.
     * @return Unit Type.
     */
    public static UnitType getType(final String name) {
        return UNIT_PROPS.get(name).type;
    }

    /**
     * Return what type of attack a card has.
     * If it returns true it means card affects their own cards / rows.
     *
     * @param name Name of Card
     * @return True if card is friendly.
     */
    public static boolean isFriendly(final String name) {
        return UNIT_PROPS.get(name).friendly;
    }
}
