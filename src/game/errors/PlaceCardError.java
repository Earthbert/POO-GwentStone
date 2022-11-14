package game.errors;

public class PlaceCardError {
    private final String command = "placeCard";
    private final int handIdx;
    private final String error;

    public PlaceCardError(final int handIdx, final String error) {
        this.handIdx = handIdx;
        this.error = error;
    }

    public final String getCommand() {
        return command;
    }

    public final int getHandIdx() {
        return handIdx;
    }

    public final String getError() {
        return error;
    }
}
