package cards.specialcards;

import cards.Minion;
import fileio.CardInput;

public class TheCursedOne extends Minion implements SpecialCard{
    public TheCursedOne(CardInput card) {
        super(card);
    }

    @Override
    public void useAbility(Minion subject) {
        int tmp = subject.getAttackDamage();
        subject.setAttackDamage(subject.getHealth());
        subject.setHealth(tmp);
        attacked = true;
    }
}
