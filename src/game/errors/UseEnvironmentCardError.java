package game.errors;

public class UseEnvironmentCardError {
    private final String command = "useEnvironmentCard";
    private final int handIdx;
    private final int affectedRow;
    private final String error;

    public UseEnvironmentCardError (int handIdx, int affectedRow, String error) {
        this.handIdx = handIdx;
        this.affectedRow = affectedRow;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getError() {
        return error;
    }
}
