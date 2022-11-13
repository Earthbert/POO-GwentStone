package game.errors;

public class PlaceCardError {
    private final String command = "placeCard";
    private final int handIdx;
    private final String error;

    public PlaceCardError(int handIdx, String error) {
        this.handIdx = handIdx;
        this.error = error;
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
