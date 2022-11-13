package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class Miraj extends Minion implements SpecialCard{
    public Miraj(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Minion subject) {
        int tmp = this.health;
        this.health = subject.getHealth();
        subject.setHealth(tmp);
    }
}
