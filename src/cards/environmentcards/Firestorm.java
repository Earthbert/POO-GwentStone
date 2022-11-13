package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class Firestorm extends Card implements Environment {
    public Firestorm(CardInput card) {
        super(card);
    }

    @Override
    public void useEnvAbility(Row row) {
        for (Minion m : row.getCardsOnRow()) {
            m.setHealth(m.getHealth() - 1);
        }
    }
}
