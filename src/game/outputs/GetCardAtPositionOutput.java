package game.outputs;

import cards.Card;
import helpers.Errors;

public class GetCardAtPositionOutput {
    private final String command = "getCardAtPosition";
    private final int x, y;
    private final Object output;

    public GetCardAtPositionOutput(final int x, final int y, final Card card) {
        this.x = x;
        this.y = y;
        if (card != null) {
            this.output = card.clone();
        } else {
            output = Errors.NO_CARD_POS;
        }
    }

    public final String getCommand() {
        return command;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final Object getOutput() {
        return output;
    }
}
