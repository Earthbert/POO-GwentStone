package game.outputs;

import cards.Card;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class GetCardsOnTableOutput {
    private final String command = "getCardsOnTable";
    private final List<List<Card>> rows;

    public GetCardsOnTableOutput(List<List<Card>> rows) {
        this.rows = rows;
    }

    public String getCommand() {
        return command;
    }

    @JsonGetter("output")
    public List<List<Card>> getRows() {
        return rows;
    }
}
