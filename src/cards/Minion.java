package cards;

import table.UnitType;

public class Minion extends Card implements Attackable {
    private int health;
    private int attackDamage;

    private boolean attacked;

    private int frozen;

    UnitType position;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean isFrozen() {
        return frozen > 0;
    }

    public void frezze() {
        frozen = 2;
    }

    public boolean isAttacked() {
        return attacked;
    }

    public boolean takeDamage(int damage) {
        health -= damage;
        attacked = true;
        return health <= 0;
    }
    
    public void attack(Attackable card) {
        card.takeDamage(attackDamage);
    }
}
