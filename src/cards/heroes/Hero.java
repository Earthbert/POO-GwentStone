package cards.heroes;

import cards.Attackable;
import cards.Card;
import fileio.CardInput;
import table.Row;

public abstract class Hero extends Card implements Attackable {
    private int health = 30;

    public Hero(CardInput card) {
        super(card);
    }

    public int getHealth() {
        return health;
    }

    public boolean takeDamage (int damage) {
        health -= damage;
        return health <= 0;
    }

    abstract void useAbility (Row row);

    @Override
    public String toString() {
        return "{"
            +  "mana="
            + mana
            +  ", description='"
            + description
            + '\''
            + ", colors="
            + colors
            + ", name='"
            +  ""
            + name
            + '\''
            + ", health="
            + health
            + '}';
    }
}
