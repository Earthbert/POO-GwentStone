package game.outputs;

public class GetPlayerWinsOutput {
    private final String command;
    private final int output;

    public GetPlayerWinsOutput(int playerIdx, int output) {
        if (playerIdx == 1)
            command = "getPlayerOneWins";
        else
            command = "getPlayerTwoWins";
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public int getOutput() {
        return output;
    }
}
