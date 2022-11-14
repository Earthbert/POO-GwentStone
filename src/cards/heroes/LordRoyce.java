package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class LordRoyce extends Hero {

    public LordRoyce(final CardInput card) {
        super(card);
    }

    private LordRoyce(final Card card) {
        super((Hero) card);
    }

    /**
     * Creates a deep copy of a LordRoyce card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new LordRoyce(this);
    }

    /**
     * Use ability of hero cards.
     * Freezes the cards with the most damageAttack.
     * @param row affected row
     */
    @Override
    public void useAbility(final Row row) {
        final List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (final Minion m : minions) {
            if (minion.getAttackDamage() <= m.getAttackDamage()) {
                minion = m;
            }
        }
        minion.freeze();
        attacked = true;
    }
}
