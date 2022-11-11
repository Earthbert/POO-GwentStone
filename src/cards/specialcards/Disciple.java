package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class Disciple extends Minion implements SpecialCard{
    public Disciple(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Minion subject) {

    }
}
