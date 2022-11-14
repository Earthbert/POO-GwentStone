package cards;

/**
 * A card that can take damage.
 */
public interface Attackable {
    /**
     * Take damage.
     *
     * @param damage damage taken
     */
    void takeDamage(int damage);
}
