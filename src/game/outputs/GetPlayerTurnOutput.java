package game.outputs;

import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerTurnOutput {
    private final String command = "getPlayerTurn";
    private final int activePlayer;

    public GetPlayerTurnOutput(final int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public final String getCommand() {
        return command;
    }

    @JsonGetter("output")
    public final int getActivePlayer() {
        return activePlayer;
    }
}
