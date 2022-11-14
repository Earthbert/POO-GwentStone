package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class Miraj extends Minion implements SpecialCard {
    public Miraj(final CardInput card) {
        super(card);
    }

    /**
     * Use Miraj ability.
     * Swaps health between his health and target health.
     * @param target target card
     */
    @Override
    public void useAbility(final Minion target) {
        final int tmp = this.health;
        this.health = target.getHealth();
        target.setHealth(tmp);
        attacked = true;
    }
}
