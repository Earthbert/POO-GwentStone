package game.outputs;

import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerTurnOutput {
    private final String command = "getPlayerTurn";
    private final int activePlayer;

    public GetPlayerTurnOutput(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public String getCommand() {
        return command;
    }

    @JsonGetter("output")
    public int getActivePlayer() {
        return activePlayer;
    }
}
