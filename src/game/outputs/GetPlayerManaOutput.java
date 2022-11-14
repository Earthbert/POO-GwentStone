package game.outputs;

import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerManaOutput {
    private final String command = "getPlayerMana";
    private final int playerIdx;
    private final int mana;

    public GetPlayerManaOutput(final int playerIdx, final int mana) {
        this.playerIdx = playerIdx;
        this.mana = mana;
    }

    public final String getCommand() {
        return command;
    }

    public final int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public final int getMana() {
        return mana;
    }
}
