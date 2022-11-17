package helpers;

final public class Errors {
    private Errors() {
    }

    public static final String PLACE_ENV = "Cannot place environment card on table.";
    public static final String NO_MANA = "Not enough mana to place card on table.";
    public static final String FULL_ROW = "Cannot place card on table since row is full.";
    public static final String INVALID_ATTACK = "Attacked card does not belong to the enemy.";
    public static final String ALREADY_ATTACKED = "Attacker card has already attacked this turn.";
    public static final String FROZEN_ATTACKER = "Attacker card is frozen.";
    public static final String NOT_TANK = "Attacked card is not of type 'Tank'.";
    public static final String INVALID_SUPPORT =
        "Attacked card does not belong to the current player.";
    public static final String NO_MANA_H = "Not enough mana to use hero's ability.";
    public static final String ALREADY_ATTACKED_H = "Hero has already attacked this turn.";
    public static final String ROW_ENEMY = "Chosen row does not belong to the enemy.";
    public static final String ROW_ENEMY_H = "Selected row does not belong to the enemy.";
    public static final String ROW_PLAYER = "Selected row does not belong to the current player.";
    public static final String NOT_ENV = "Chosen card is not of type environment.";
    public static final String NO_MANA_E = "Not enough mana to use environment card.";
    public static final String HEART_HOUND =
        "Cannot steal enemy card since the player's row is full.";
    public static final String INVALID_CMD = "Invalid Command : This shouldn't happen";
    public static final String NO_CARD_POS = "No card available at that position.";
    public static final String PLAYER_ONE_KILL = "Player one killed the enemy hero.";
    public static final String PLAYER_TWO_KILL = "Player two killed the enemy hero.";
}
