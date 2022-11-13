package cards.heroes;

import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class EmpressThorina extends Hero{

    public EmpressThorina(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Row row) {
        List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (Minion m : minions) {
            if (minion.getHealth() <= m.getHealth()) {
                minion = m;
            }
        }
        minion.takeDamage(minion.getHealth());
    }
}
