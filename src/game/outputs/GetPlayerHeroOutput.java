package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerHeroOutput {
    private final String command = "getPlayerHero";
    private final int playerIdx;
    private final Card hero;

    public GetPlayerHeroOutput(final int playerIdx, final Card hero) {
        this.playerIdx = playerIdx;
        this.hero = hero.clone();
    }

    public final String getCommand() {
        return command;
    }

    public final int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public final Card getHero() {
        return hero;
    }
}
