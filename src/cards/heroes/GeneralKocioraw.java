package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

public class GeneralKocioraw extends Hero{

    public GeneralKocioraw(CardInput card) {
        super(card);
    }

    private GeneralKocioraw(Card card) {
        super((Hero) card);
    }

    @Override
    public Card clone() {
        return new GeneralKocioraw(this);
    }

    @Override
    public void useAbility(Row row) {
        for (Minion minion : row.getCardsOnRow()) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
        attacked = true;
    }
}
