package game.outputs;

import cards.Minion;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class GetCardsOnTableOutput {
    private final String command = "getCardsOnTable";
    private final List<List<Minion>> rows;

    public GetCardsOnTableOutput(List<List<Minion>> rows) {
        this.rows = rows;
    }

    public String getCommand() {
        return command;
    }

    @JsonGetter("output")
    public List<List<Minion>> getRows() {
        return rows;
    }
}
