package game.outputs;

public class GetPlayerWinsOutput {
    private final String command;
    private final int output;

    public GetPlayerWinsOutput(final int playerIdx, final int output) {
        if (playerIdx == 1) {
            command = "getPlayerOneWins";
        } else {
            command = "getPlayerTwoWins";
        }
        this.output = output;
    }

    public final String getCommand() {
        return command;
    }

    public final int getOutput() {
        return output;
    }
}
