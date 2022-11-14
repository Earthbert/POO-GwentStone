package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class GeneralKocioraw extends Hero {

    public GeneralKocioraw(final CardInput card) {
        super(card);
    }

    private GeneralKocioraw(final Card card) {
        super((Hero) card);
    }

    /**
     * Creates a deep copy of a GeneralKocioraw card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new GeneralKocioraw(this);
    }

    /**
     * Use ability of hero cards.
     * Gives +1 damageAttack to all cards on a row.
     * @param row affected row
     */
    @Override
    public void useAbility(final Row row) {
        for (final Minion minion : row.getCardsOnRow()) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
        attacked = true;
    }
}
