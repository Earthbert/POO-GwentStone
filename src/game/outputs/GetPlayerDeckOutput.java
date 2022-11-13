package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class GetPlayerDeckOutput {
    private final String command = "getPlayerDeck";
    private final int playerIdx;
    private final List<Card> cards;

    public GetPlayerDeckOutput(int playerIdx, List<Card> cards) {
        this.playerIdx = playerIdx;
        this.cards = cards;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public List<Card> getCards() {
        return cards;
    }
}
