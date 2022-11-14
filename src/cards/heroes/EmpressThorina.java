package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class EmpressThorina extends Hero {

    public EmpressThorina(final CardInput card) {
        super(card);
    }

    private EmpressThorina(final Card card) {
        super((Hero) card);
    }

    /**
     * Creates a deep copy of a EmpressThorina card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new EmpressThorina(this);
    }

    /**
     * Use ability of hero cards.
     * Destroys the card with the most health from a row.
     * @param row affected row
     */
    @Override
    public void useAbility(final Row row) {
        final List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (final Minion m : minions) {
            if (minion.getHealth() <= m.getHealth()) {
                minion = m;
            }
        }
        minion.takeDamage(minion.getHealth());
        attacked = true;
    }
}
