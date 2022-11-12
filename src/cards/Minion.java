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
    private boolean frozen;


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
        return frozen;
    }

    public void frezze() {
        frozen = true;
    }

    public boolean isTank() {
        return tank;
    }

    public boolean hasAttacked() {
        return attacked;
    }


    /**
     * Prepare card for next turn.
     * Clears frozen state, puts attacked to false
     */
    public void prepareCard() {
        attacked = false;
        frozen = false;
    }

    public boolean takeDamage(int damage) {
        health -= damage;
        attacked = true;
        return health <= 0;
    }
    
    public void attack(Attackable card) {
        card.takeDamage(attackDamage);
    }

    @Override
    public String toString() {
        return "{"
            +  "mana="
            + mana
            +  ", attackDamage="
            + attackDamage
            + ", health="
            + health
            +  ", description='"
            + description
            + '\''
            + ", colors="
            + colors
            + ", name='"
            +  ""
            + name
            + '\''
            + '}';
    }
}
