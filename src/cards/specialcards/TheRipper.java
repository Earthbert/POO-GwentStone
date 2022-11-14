package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class TheRipper extends Minion implements SpecialCard {
    public TheRipper(final CardInput card) {
        super(card);
    }

    /**
     * Use Disciple ability.
     * Gives -2 damage to target.
     * @param target target card
     */
    @Override
    public void useAbility(final Minion target) {
        target.setAttackDamage(target.getAttackDamage() - 2);
        attacked = true;
    }
}
