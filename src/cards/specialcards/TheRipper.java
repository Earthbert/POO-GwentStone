package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class TheRipper extends Minion implements SpecialCard{
    public TheRipper(CardInput card) {
        super(card);
    }

    @Override
    public boolean useAbility(Minion subject) {

    }
}
