package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class GetEnvironmentCardsInHandOutput {
    private final String command = "getEnvironmentCardsInHand";
    private final int playerIdx;
    private final List<Card> cards;

    public GetEnvironmentCardsInHandOutput(final int playerIdx, final List<Card> cards) {
        this.playerIdx = playerIdx;
        this.cards = cards;
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
