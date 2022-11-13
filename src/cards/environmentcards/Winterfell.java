package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class Winterfell extends Card implements Environment {
    public Winterfell(CardInput card) {
        super(card);
    }

    @Override
    public void useEnvAbility(Row row) {
        for (Minion m : row.getCardsOnRow()) {
            m.freeze();
        }
    }
}
