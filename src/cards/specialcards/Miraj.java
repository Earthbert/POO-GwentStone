package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class Miraj extends Minion implements SpecialCard{
    public Miraj(CardInput card) {
        super(card);
    }

    @Override
    public boolean useAbility(Minion subject) {

    }
}
