package game.errors;

import fileio.Coordinates;

public class UseAttackHeroError {
    private final String command = "useAttackHero";
    private final Coordinates cardAttacker;
    private final String error;

    public UseAttackHeroError(final Coordinates cardAttacker, final String error) {
        this.cardAttacker = cardAttacker;
        this.error = error;
    }

    public final String getCommand() {
        return command;
    }

    public final Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public final String getError() {
        return error;
    }
}
