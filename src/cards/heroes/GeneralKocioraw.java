package cards.heroes;

import cards.Minion;
import fileio.CardInput;
import table.Row;

public class GeneralKocioraw extends Hero{

    public GeneralKocioraw(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Row row) {
        for (Minion minion : row.getCardsOnRow()) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }
}
