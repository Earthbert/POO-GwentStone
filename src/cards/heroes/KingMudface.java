package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class KingMudface extends Hero {

    public KingMudface(final CardInput card) {
        super(card);
    }

    private KingMudface(final Card card) {
        super((Hero) card);
    }

    /**
     * Creates a deep copy of a KingMudface card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new KingMudface(this);
    }

    /**
     * Use ability of hero cards.
     * Gives +1 health to all cards on a row.
     * @param row affected row
     */
    @Override
    public void useAbility(final Row row) {
        for (final Minion minion : row.getCardsOnRow()) {
            minion.setHealth(minion.getHealth() + 1);
        }
        attacked = true;
    }
}
