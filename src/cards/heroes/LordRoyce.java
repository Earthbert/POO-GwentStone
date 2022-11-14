package cards.heroes;

import cards.Card;
import cards.Minion;
import fileio.CardInput;
import table.Row;

import java.util.List;

public class LordRoyce extends Hero{

    public LordRoyce(CardInput card) {
        super(card);
    }

    private LordRoyce(Card card) {
        super((Hero) card);
    }

    @Override
    public Card clone() {
        return new LordRoyce(this);
    }

    @Override
    public void useAbility(Row row) {
        List<Minion> minions = row.getCardsOnRow();
        Minion minion = minions.get(0);
        for (Minion m : minions) {
            if (minion.getAttackDamage() <= m.getAttackDamage()) {
                minion = m;
            }
        }
        minion.freeze();
        attacked = true;
    }
}
