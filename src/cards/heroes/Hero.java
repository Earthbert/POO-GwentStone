package cards.heroes;

import cards.Attackable;
import cards.Card;
import fileio.CardInput;
import table.Row;

public abstract class Hero extends Card implements Attackable {
    private int health = 30;
    private boolean attacked = false;

    public Hero(CardInput card) {
        super(card);
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage (int damage) {
        health -= damage;
        //TODO THROW HERO DEAD
    }

    public boolean hasAttacked() {
        return attacked;
    }

    public abstract void useAbility (Row row);
}
