package utils;

import java.util.HashMap;

import static utils.ErrorType.*;

public class Error {
    private static final HashMap<ErrorType, String> errorStrings = new HashMap<>();

    static {
        errorStrings.put(PLACE_ENV, "Cannot place environment card on table.");
        errorStrings.put(NO_MANA, "Not enough mana to place card on table.");
        errorStrings.put(FULL_ROW, "Cannot place card on table since row is full.");
        errorStrings.put(INVALID_ATTACK, "Attacked card does not belong to the enemy.");
        errorStrings.put(ALREADY_ATTACKED, "Attacker card has already attacked this turn.");
        errorStrings.put(FROZEN_ATTACKER, "Attacker card is frozen.");
        errorStrings.put(NOT_TANK, "Attacked card is not of type 'Tank’.");
        errorStrings.put(INVALID_HEAL, "Attacked card does not belong to the current player.");
        errorStrings.put(NO_MANA_H, "Not enough mana to use hero's ability.");
        errorStrings.put(ALREADY_ATTACKED_H, "Hero has already attacked this turn.");
        errorStrings.put(ROW_ENEMY, "Chosen row does not belong to the enemy.");
        errorStrings.put(ROW_PLAYER, "Selected row does not belong to the current player.");
        errorStrings.put(NOT_ENV, "Chosen card is not of type environment.");
        errorStrings.put(NO_MANA_E, "Not enough mana to use environment card.");
        errorStrings.put(HEART_HOUND, "Cannot steal enemy card since the player's row is full.");
    }

    public static String getString(ErrorType type) {
        return errorStrings.get(type);
    }
}
