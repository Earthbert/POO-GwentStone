package cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;
import table.Row;
import helpers.UnitPos;
import helpers.UnitProp;

@JsonPropertyOrder(value = {"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card implements Attackable {
    protected int health;
    protected int attackDamage;

    private Row row;

    @JsonIgnore
    private final UnitPos position;
    @JsonIgnore
    private final boolean tank;

    @JsonIgnore
    protected boolean attacked;
    @JsonIgnore
    private boolean frozen;


    public Minion(final CardInput card) {
        super(card);
        this.position = UnitProp.getPosition(name);
        this.tank = UnitProp.isTank(name);
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
    }

    protected Minion(final Minion copied) {
        super(copied);
        this.health = copied.health;
        this.attackDamage = copied.attackDamage;
        this.row = copied.row;
        this.position = copied.position;
        this.tank = copied.tank;
        this.attacked = copied.attacked;
        this.frozen = copied.frozen;
    }

    /**
     * Creates a deep copy of a Minion card.
     * Uses protected copy constructor.
     * @return new Card
     */
    @Override
    public Card clone() {
        return new Minion(this);
    }

    public final int getHealth() {
        return health;
    }

    /**
     * Sets health.
     * If the minion dies it is removed from this row.
     * @param health new health
     */
    public final void setHealth(final int health) {
        this.health = health;
        if (health <= 0) {
            row.removeCard(this);
        }
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = Math.max(attackDamage, 0);
    }

    public final boolean isFrozen() {
        return frozen;
    }

    /**
     * Freeze this minion.
     */
    public final void freeze() {
        frozen = true;
    }

    public final boolean isTank() {
        return tank;
    }

    /**
     * Check if minion has attacked.
     * @return return true if this minion has attacked this turn.
     */
    public final boolean hasAttacked() {
        return attacked;
    }

    public final UnitPos getPosition() {
        return position;
    }

    /**
     * It is called when the minion is placed on a row.
     * It is useful that the minion "knows" where it is.
     * @param row the row on which is placed.
     */
    public final void setRow(final Row row) {
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

    /**
     * Take damage.
     * If the minion dies it is removed from this row.
     * @param damage damage taken
     */
    public void takeDamage(final int damage) {
        health -= damage;
        if (health <= 0) {
            row.removeCard(this);
        }
    }

    /**
     * Attack an attackable card.
     * @param card attacked card
     */
    public void attack(final Attackable card) {
        card.takeDamage(attackDamage);
        this.attacked = true;
    }
}
