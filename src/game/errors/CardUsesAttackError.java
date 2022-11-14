package game.errors;

import fileio.Coordinates;

public class CardUsesAttackError {
    private final String command = "cardUsesAttack";
    private final Coordinates cardAttacker;
    private final Coordinates cardAttacked;
    private final String error;

    public CardUsesAttackError(final Coordinates cardAttacker,
                               final Coordinates cardAttacked, final String error) {
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.error = error;
    }

    public final String getCommand() {
        return command;
    }

    public final Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public final Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public final String getError() {
        return error;
    }
}
