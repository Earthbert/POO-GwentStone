package game.errors;

public class UseEnvironmentCardError {
    private final String command = "useEnvironmentCard";
    private final int handIdx;
    private final int affectedRow;
    private final String error;

    public UseEnvironmentCardError(final int handIdx, final int affectedRow, final String error) {
        this.handIdx = handIdx;
        this.affectedRow = affectedRow;
        this.error = error;
    }

    public final String getCommand() {
        return command;
    }

    public final int getHandIdx() {
        return handIdx;
    }

    public final int getAffectedRow() {
        return affectedRow;
    }

    public final String getError() {
        return error;
    }
}
