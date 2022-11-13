package game.errors;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

public class UseAttackHeroError {
    private final String command = "useAttackHero";
    private final Coordinates cardAttacker;
    private final String error;

    public UseAttackHeroError(Coordinates cardAttacker, String error, ArrayNode output) {
        this.cardAttacker = cardAttacker;
        this.error = error;
        output.addPOJO(this);
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
