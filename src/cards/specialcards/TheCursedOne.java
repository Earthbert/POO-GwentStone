package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class TheCursedOne extends Minion implements SpecialCard{
    public TheCursedOne(CardInput card) {
        super(card);
    }

    @Override
    public boolean useAbility(Minion subject) {

    }
}
