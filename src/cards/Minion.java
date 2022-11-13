package cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import table.Row;
import utils.UnitPos;
import utils.UnitProp;

public class Minion extends Card implements Attackable {
    protected int health;
    protected int attackDamage;

    private Row row;

    @JsonIgnore
    private final UnitPos position;
    @JsonIgnore
    private final boolean tank;

    @JsonIgnore
    private boolean attacked;
    @JsonIgnore
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

    public UnitPos getPosition() {
        return position;
    }

    public void placedCard(Row row) {
        this.row = row;
    }

    /**
     * Prepare card for next turn.
     * Clears frozen state, puts attacked to false
     */
    public void prepareCard() {
        attacked = false;
        frozen = false;
    }

    public void takeDamage(int damage) {
        health -= damage;
        attacked = true;
        if(health <= 0)
            row.removeCard(this);
    }
    
    public void attack(Attackable card) {
        card.takeDamage(attackDamage);
    }
}
