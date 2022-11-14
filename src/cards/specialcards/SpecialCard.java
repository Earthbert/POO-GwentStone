package cards.specialcards;

import cards.Minion;

public interface SpecialCard {
    /**
     * Use hero ability
     * Effect depends on Card type.
     * @param target target card
     */
    void useAbility(Minion target);
}
