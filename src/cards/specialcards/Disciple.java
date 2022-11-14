package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class Disciple extends Minion implements SpecialCard {
    public Disciple(final CardInput card) {
        super(card);
    }

    /**
     * Use Disciple ability.
     * Gives +2 health to target.
     * @param target target card
     */
    @Override
    public void useAbility(final Minion target) {
        target.setHealth(target.getHealth() + 2);
        attacked = true;
    }
}
