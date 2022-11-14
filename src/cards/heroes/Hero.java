package cards.heroes;

import cards.Attackable;
import cards.Card;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;
import table.Row;
import utils.ExceptionWonGame;

@JsonPropertyOrder(value = {"mana", "description", "colors", "name", "health"})
public abstract class Hero extends Card implements Attackable {
    private int health = 30;
    protected boolean attacked = false;

    public Hero(CardInput card) {
        super(card);
    }

    public Hero(Hero copied) {
        super(copied);
        this.health = copied.health;
        this.attacked = copied.attacked;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage (int damage) {
        health -= damage;
        if (health <= 0)
            throw new ExceptionWonGame();
    }

    public void prepareHero() {
        this.attacked = false;
    }

    public boolean hasAttacked() {
        return attacked;
    }

    public abstract void useAbility (Row row);
}
