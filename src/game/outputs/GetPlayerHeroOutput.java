package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerHeroOutput {
    private final String command = "getPlayerHero";
    private final int playerIdx;
    private final Card hero;

    public GetPlayerHeroOutput(int playerIdx, Card hero) {
        this.playerIdx = playerIdx;
        this.hero = hero.clone();
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public Card getHero() {
        return hero;
    }
}
