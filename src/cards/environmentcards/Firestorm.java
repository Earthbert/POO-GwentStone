package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.LinkedList;
import java.util.List;

public class Firestorm extends Card implements Environment {
    public Firestorm(final CardInput card) {
        super(card);
    }

    private Firestorm(final Card card) {
        super(card);
    }

    /**
     * Creates a deep copy of a Firestorm card.
     * Uses private copy constructor.
     * @return new Card
     */
    @Override
    public Card clone() {
        return new Firestorm(this);
    }

    /**
     * Use environment ability of a Firestorm card.
     * Gives 1 damage to all cards on a row.
     * @param row affected row
     */
    @Override
    public void useEnvAbility(final Row row) {
        final List<Minion> cardsOnRow = new LinkedList<>(row.getCardsOnRow());
        for (final Minion m : cardsOnRow) {
            m.takeDamage(1);
        }
    }
}
