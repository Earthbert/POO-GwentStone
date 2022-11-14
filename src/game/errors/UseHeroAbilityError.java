package game.errors;

public class UseHeroAbilityError {
    private final String command = "useHeroAbility";
    private final int affectedRow;
    private final String error;

    public UseHeroAbilityError(final int affectedRow, final String error) {
           this.affectedRow = affectedRow;
           this.error = error;
    }

    public final String getCommand() {
        return command;
    }

    public final int getAffectedRow() {
        return affectedRow;
    }

    public final String getError() {
        return error;
    }
}
