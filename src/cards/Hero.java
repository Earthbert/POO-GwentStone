package cards;

import table.Row;

public abstract class Hero extends Card implements Attackable{
    private int health = 30;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean takeDamage (int damage) {
        health -= damage;
        return health <= 0;
    }

    abstract void useAbility (Row row);
}
