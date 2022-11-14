package game.outputs;

import cards.Card;
import cards.Minion;

public class GetCardAtPositionOutput {
    private final String command = "getCardAtPosition";
    private final int x, y;
    private final Object output;

    public GetCardAtPositionOutput(int x, int y, Card card) {
        this.x = x;
        this.y = y;
        if (card != null)
            this.output = new Minion((Minion) card);
        else
            output = "No card available at that position.";
    }

    public String getCommand() {
        return command;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Object getOutput() {
        return output;
    }
}
