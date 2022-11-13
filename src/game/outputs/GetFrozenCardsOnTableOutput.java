package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class GetFrozenCardsOnTableOutput {
    private final String command = "getFrozenCardsOnTable";
    private final List<Card> cards;

    public GetFrozenCardsOnTableOutput(List<Card> cards) {
        this.cards = cards;
    }

    public String getCommand() {
        return command;
    }

    @JsonGetter("output")
    public List<Card> getCards() {
        return cards;
    }
}
