package game.outputs;

public class GetTotalGamesPlayedOutput {
    private final String command = "getTotalGamesPlayed";
    private final int output;

    public GetTotalGamesPlayedOutput(final int output) {
        this.output = output;
    }

    public final String getCommand() {
        return command;
    }

    public final int getOutput() {
        return output;
    }
}
