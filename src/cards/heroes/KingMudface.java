package cards.heroes;

import cards.Minion;
import fileio.CardInput;
import table.Row;

public class KingMudface extends Hero {

    public KingMudface(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Row row) {
        for (Minion minion : row.getCardsOnRow()) {
            minion.setHealth(minion.getHealth() + 1);
        }
        attacked = true;
    }
}
