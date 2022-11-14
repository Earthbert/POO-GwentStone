package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class Winterfell extends Card implements Environment {
    public Winterfell(final CardInput card) {
        super(card);
    }

    private Winterfell(final Card card) {
        super(card);
    }

    /**
     * Creates a deep copy of a Winterfell card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new Winterfell(this);
    }

    /**
     * Use environment ability of a Winterfell card.
     * Freezes all cards on a row.
     * @param row affected row
     */
    @Override
    public void useEnvAbility(final Row row) {
        for (final Minion m : row.getCardsOnRow()) {
            m.freeze();
        }
    }
}
