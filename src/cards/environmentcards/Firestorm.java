package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.LinkedList;
import java.util.List;

public class Firestorm extends Card implements Environment {
    public Firestorm(CardInput card) {
        super(card);
    }

    private Firestorm(Card card) {
        super(card);
    }

    @Override
    public Card clone() {
        return new Firestorm(this);
    }

    @Override
    public void useEnvAbility(Row row) {
        List<Minion> cardsOnRow = new LinkedList<>(row.getCardsOnRow());
        for (Minion m : cardsOnRow) {
            m.takeDamage(1);
        }
    }
}
