package game.errors;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class UseHeroAbilityError {
    private final String command = "useHeroAbility";
    private final int affectedRow;
    private final String error;

    public UseHeroAbilityError (int affectedRow, String error, ArrayNode output) {
           this.affectedRow = affectedRow;
           this.error = error;
           output.addPOJO(this);
    }

    public String getCommand() {
        return command;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getError() {
        return error;
    }
}
