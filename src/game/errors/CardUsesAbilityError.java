package game.errors;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

public class CardUsesAbilityError {
    private final String command = "cardUsesAbility";
    private final Coordinates cardAttacker;
    private final Coordinates cardAttacked;
    private final String error;

    public CardUsesAbilityError(Coordinates cardAttacker, Coordinates cardAttacked, String error, ArrayNode output) {
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.error = error;
        output.addPOJO(this);
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public String getError() {
        return error;
    }
}
