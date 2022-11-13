package game.outputs;

import cards.heroes.Hero;
import com.fasterxml.jackson.annotation.JsonGetter;

public class GetPlayerHeroOutput {
    private final String command = "getPlayerHero";
    private final int playerIdx;
    private final Hero hero;

    public GetPlayerHeroOutput(int playerIdx, Hero hero) {
        this.playerIdx = playerIdx;
        this.hero = hero;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public Hero getHero() {
        return hero;
    }
}
