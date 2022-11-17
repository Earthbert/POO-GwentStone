package helpers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import static helpers.UnitType.*;
import static helpers.UnitPos.*;
import cards.*;
import cards.specialcards.*;
import cards.environmentcards.*;
import cards.heroes.*;
import fileio.CardInput;
import lombok.SneakyThrows;

public final class UnitProp {
    private UnitProp() {
    }

    private static class Proprieties {
        final UnitType type;
        final UnitPos position;
        final boolean tank;
        final boolean support;
        final Class<?> clazz;

        Proprieties(final UnitType type, final UnitPos position,
                    final boolean tank, final boolean support, final Class<?> clazz) {
            this.type = type;
            this.position = position;
            this.tank = tank;
            this.support = support;
            this.clazz = clazz;
        }
    }

    private static final HashMap<String, Proprieties> UNIT_PROPS = new HashMap<>();

    static {
        // Standard Cards//
        UNIT_PROPS.put("Sentinel", new Proprieties(STANDARD, RANGED, false, false, Minion.class));
        UNIT_PROPS.put("Berserker", new Proprieties(STANDARD, RANGED, false, false, Minion.class));
        UNIT_PROPS.put("Goliath", new Proprieties(STANDARD, CLOSE, true, false, Minion.class));
        UNIT_PROPS.put("Warden", new Proprieties(STANDARD, CLOSE, true, false, Minion.class));
        // Special Cards //
        UNIT_PROPS.put("The Ripper", new Proprieties(SPECIAL, CLOSE,
            false, false, TheRipper.class));
        UNIT_PROPS.put("Miraj", new Proprieties(SPECIAL, CLOSE, false, false, Miraj.class));
        UNIT_PROPS.put("The Cursed One", new Proprieties(SPECIAL, RANGED,
            false, false, TheCursedOne.class));
        UNIT_PROPS.put("Disciple", new Proprieties(SPECIAL, RANGED, false, true, Disciple.class));
        // Environment Cards //
        UNIT_PROPS.put("Firestorm", new Proprieties(ENVIRONMENT, null,
            false, false, Firestorm.class));
        UNIT_PROPS.put("Winterfell", new Proprieties(ENVIRONMENT, null,
            false, false, Winterfell.class));
        UNIT_PROPS.put("Heart Hound", new Proprieties(ENVIRONMENT, null,
            false, false, HeartHound.class));
        // Hero Cards //
        UNIT_PROPS.put("Empress Thorina", new Proprieties(HERO, null,
            false, false, EmpressThorina.class));
        UNIT_PROPS.put("General Kocioraw", new Proprieties(HERO, null,
            false, true, GeneralKocioraw.class));
        UNIT_PROPS.put("King Mudface", new Proprieties(HERO, null,
            false, true, KingMudface.class));
        UNIT_PROPS.put("Lord Royce", new Proprieties(HERO, null, false, false, LordRoyce.class));
    }

    /**
     * Checks if a card is tank.
     * It is used at Card creation.
     * @param name Name of Card.
     * @return True if unit is tank, false otherwise.
     */
    public static boolean isTank(final String name) {
        if (!UNIT_PROPS.containsKey(name)) {
            exit(name);
        }
        return UNIT_PROPS.get(name).tank;
    }

    /**
     * Return table position of a card.
     * It is used at Card creation.
     * @param name Name of Card.
     * @return Table Position.
     */
    public static UnitPos getPosition(final String name) {
        if (!UNIT_PROPS.containsKey(name)) {
            exit(name);
        }
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
        if (!UNIT_PROPS.containsKey(name)) {
            exit(name);
        }
        return UNIT_PROPS.get(name).type;
    }

    /**
     * Return what type of attack a card has.
     * If it returns true it means card affects their own cards / rows.
     *
     * @param name Name of Card
     * @return True if card is friendly.
     */
    public static boolean isSupport(final String name) {
        if (!UNIT_PROPS.containsKey(name)) {
            exit(name);
        }
        return UNIT_PROPS.get(name).support;
    }

    @SneakyThrows
    public static Constructor<?> getCtor(final String name) {
        if (!UNIT_PROPS.containsKey(name)) {
            exit(name);
        }
        return UNIT_PROPS.get(name).clazz.getConstructor(CardInput.class);
    }

    private static void exit(final String card) {
        throw new ExceptionInvalidCard("ERROR: Card {" + card + "} doesn't exit.");
    }
}
