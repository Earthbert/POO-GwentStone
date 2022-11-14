package cards.environmentcards;

import table.Row;

/**
 * Interface for environment type cards.
 */
public interface Environment {
    /**
     * Use environment ability of a card.
     *
     * @param row affected row
     */
    void useEnvAbility(Row row);
}
