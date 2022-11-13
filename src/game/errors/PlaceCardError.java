package game.errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PlaceCardError {
    private final String command = "placeCard";
    private final int handIdx;
    private final String error;

    public PlaceCardError(int handIdx, String error, ArrayNode output) {
        this.handIdx = handIdx;
        this.error = error;
        output.addPOJO(this);
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public String getError() {
        return error;
    }
}
