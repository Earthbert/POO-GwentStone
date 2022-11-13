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
        for (int i = 0; i < row.getCardsOnRow().size(); ) {
            Minion m = row.getCardsOnRow().get(i);
            if (!m.takeDamage(1))
                i++;
        }
    }
}
