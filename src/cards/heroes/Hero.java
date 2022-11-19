package cards.heroes;

import cards.Attackable;
import cards.Card;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;
import table.Row;
import helpers.ExceptionWonGame;

/**
 * The type Hero.
 */
@JsonPropertyOrder(value = {"mana", "description", "colors", "name", "health"})
public abstract class Hero extends Card implements Attackable {
    public static final int INITIAL_HEALTH = 30;
    private int health = INITIAL_HEALTH;
    protected boolean attacked = false;

    public Hero(final CardInput card) {
        super(card);
    }

    protected Hero(final Hero copied) {
        super(copied);
        this.health = copied.health;
        this.attacked = copied.attacked;
    }

    /**
     * Creates a deep copy of a card.
     * Uses private / protected copy constructor.
     * @return new Card
     */
    public abstract Card clone();

    public final int getHealth() {
        return health;
    }

    /**
     * Hero Card takes damage.
     * @param damage damage taken.
     * @throws ExceptionWonGame is thrown when the hero dies.
     */
    public final void takeDamage(final int damage) {
        health -= damage;
        if (health <= 0) {
            throw new ExceptionWonGame();
        }
    }

    /**
     * Prepares hero for next round.
     * Puts attack to false.
     */
    public final void prepareHero() {
        this.attacked = false;
    }

    /**
     *
     * @return return true if hero attacked this turn.
     */
    public final boolean hasAttacked() {
        return attacked;
    }

    /**
     * Use ability of hero cards.
     * Effect depends on the type of hero.
     * @param row affected row
     */
    public abstract void useAbility(Row row);
}
