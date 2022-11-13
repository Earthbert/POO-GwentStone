package game.outputs;

public class GetTotalGamesPlayedOutput {
    private final String command = "getTotalGamesPlayed";
    private final int output;

    public GetTotalGamesPlayedOutput(int output) {
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public int getOutput() {
        return output;
    }
}
