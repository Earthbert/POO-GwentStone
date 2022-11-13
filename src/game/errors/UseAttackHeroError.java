package game.errors;

import fileio.Coordinates;

public class UseAttackHeroError {
    private final String command = "useAttackHero";
    private final Coordinates cardAttacker;
    private final String error;

    public UseAttackHeroError(Coordinates cardAttacker, String error) {
        this.cardAttacker = cardAttacker;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public String getError() {
        return error;
    }
}
