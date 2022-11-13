package cards.environmentcards;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class HeartHound extends Card implements Environment {

    public HeartHound(CardInput card) {
        super(card);
    }

    @Override
    public void useEnvAbility(Row row) {
        List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (Minion m : minions) {
            if (minion.getHealth() <= m.getHealth()) {
                minion = m;
            }
        }
        row.removeCard(minion);
        if (row.player == 1)
            row.table.placeCard(minion, 2);
        else
            row.table.placeCard(minion, 1);
    }
}
