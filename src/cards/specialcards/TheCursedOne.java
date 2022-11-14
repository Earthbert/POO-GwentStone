package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class TheCursedOne extends Minion implements SpecialCard {
    public TheCursedOne(final CardInput card) {
        super(card);
    }

    /**
     * Use The Cursed One ability.
     * Swaps between his damage and target damage.
     * @param target target card
     */
    @Override
    public void useAbility(final Minion target) {
        final int tmp = target.getAttackDamage();
        target.setAttackDamage(target.getHealth());
        target.setHealth(tmp);
        attacked = true;
    }
}
