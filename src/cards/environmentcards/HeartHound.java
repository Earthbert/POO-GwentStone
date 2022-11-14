package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class HeartHound extends Card implements Environment {

    public HeartHound(final CardInput card) {
        super(card);
    }

    private HeartHound(final Card card) {
        super(card);
    }

    /**
     * Creates a deep copy of a Heart Hound card.
     * Uses private copy constructor.
     *
     * @return new Card
     */
    @Override
    public Card clone() {
        return new HeartHound(this);
    }

    /**
     * Use environment ability of a Heart Hound card.
     * Steals the card with the most health from affected row and puts it on the mirrored row.
     * @param row affected row
     */
    @Override
    public void useEnvAbility(final Row row) {
        final List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (final Minion m : minions) {
            if (minion.getHealth() < m.getHealth()) {
                minion = m;
            }
        }
        row.removeCard(minion);
        if (row.getPlayer() == 1) {
            row.getTable().placeCard(minion, 2);
        } else {
            row.getTable().placeCard(minion, 1);
        }
    }
}
