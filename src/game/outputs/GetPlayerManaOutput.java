package game.outputs;

import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerManaOutput {
    private final String command = "getPlayerMana";
    private final int playerIdx;
    private final int mana;

    public GetPlayerManaOutput(int playerIdx, int mana) {
        this.playerIdx = playerIdx;
        this.mana = mana;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public int getMana() {
        return mana;
    }
}
