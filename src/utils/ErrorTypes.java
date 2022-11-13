package utils;

import java.util.HashMap;

import static utils.ErrorType.*;

public class ErrorTypes {
    private static final HashMap<ErrorType, String> errorTypes = new HashMap<>();

    static {
        errorTypes.put(PLACE_ENV, "Cannot place environment card on table.");
        errorTypes.put(NO_MANA, "Not enough mana to place card on table.");
        errorTypes.put(FULL_ROW, "Cannot place card on table since row is full.");
        errorTypes.put(INVALID_ATTACK, "Attacked card does not belong to the enemy.");
        errorTypes.put(ALREADY_ATTACKED, "Attacker card has already attacked this turn.");
        errorTypes.put(FROZEN_ATTACKER, "Attacker card is frozen.");
        errorTypes.put(NOT_TANK, "Attacked card is not of type 'Tank’.");
        errorTypes.put(INVALID_HEAL, "Attacked card does not belong to the current player.");
        errorTypes.put(NO_MANA_H, "Not enough mana to use hero's ability.");
        errorTypes.put(ALREADY_ATTACKED_H, "Hero has already attacked this turn.");
        errorTypes.put(ROW_ENEMY, "Selected row does not belong to the enemy.");
        errorTypes.put(ROW_PLAYER, "Selected row does not belong to the current player.");
        errorTypes.put(NOT_ENV, "Chosen card is not of type environment");
        errorTypes.put(NO_MANA_E, "Not enough mana to use environment card.");
        errorTypes.put(HEART_HOUND, "Cannot steal enemy card since the player's row is full.");
    }

    public static String getType(ErrorType type) {
        return errorTypes.get(type);
    }
}
