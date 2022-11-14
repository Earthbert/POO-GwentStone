package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class GetPlayerDeckOutput {
    private final String command = "getPlayerDeck";
    private final int playerIdx;
    private final List<Card> cards;

    public GetPlayerDeckOutput(int playerIdx, List<Card> cards) {
        this.playerIdx = playerIdx;
        this.cards = new ArrayList<>();
        for (Card card : cards) {
            this.cards.add(card.clone());
        }
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
