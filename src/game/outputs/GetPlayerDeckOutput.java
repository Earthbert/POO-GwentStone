package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class GetPlayerDeckOutput {
    private final String command = "getPlayerDeck";
    private final int playerIdx;
    private final List<Card> cards;

    public GetPlayerDeckOutput(final int playerIdx, final List<Card> cards) {
        this.playerIdx = playerIdx;
        this.cards = new ArrayList<>();
        for (final Card card : cards) {
            this.cards.add(card.clone());
        }
    }

    public final String getCommand() {
        return command;
    }

    public final int getPlayerIdx() {
        return playerIdx;
    }

    @JsonGetter("output")
    public final List<Card> getCards() {
        return cards;
    }
}
