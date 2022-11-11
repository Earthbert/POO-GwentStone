package cards;

import fileio.CardInput;
import utils.UnitPos;
import utils.UnitProp;

public class Minion extends Card implements Attackable {
    protected int health;
    protected int attackDamage;

    private final UnitPos position;
    private final boolean tank;

    private boolean attacked;
    private int frozen;


    public Minion(CardInput card) {
        super(card);
        this.position = UnitProp.getPosition(name);
        this.tank = UnitProp.isTank(name);
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
    }

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

    public boolean isTank() {
        return tank;
    }
}
