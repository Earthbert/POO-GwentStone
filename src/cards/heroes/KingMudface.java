package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class KingMudface extends Hero {

    public KingMudface(CardInput card) {
        super(card);
    }

    private KingMudface(Card card) {
        super((Hero) card);
    }

    @Override
    public Card clone() {
        return new KingMudface(this);
    }

    @Override
    public void useAbility(Row row) {
        for (Minion minion : row.getCardsOnRow()) {
            minion.setHealth(minion.getHealth() + 1);
        }
        attacked = true;
    }
}
