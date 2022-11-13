package cards.heroes;

import cards.Attackable;
import cards.Card;
import fileio.CardInput;
import table.Row;
import utils.ExceptionWonGame;

public abstract class Hero extends Card {
    private int health = 30;
    protected boolean attacked = false;

    public Hero(CardInput card) {
        super(card);
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage (int damage) {
        health -= damage;
        if (health <= 0)
            throw new ExceptionWonGame();
    }

    public boolean hasAttacked() {
        return attacked;
    }

    public abstract void useAbility (Row row);
}
