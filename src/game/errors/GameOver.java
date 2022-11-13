package game.errors;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class GameOver {
    private final String gameEnded;

     public GameOver(int id, ArrayNode output) {
        if (id == 1)
            gameEnded = "Player one killed the enemy hero.";
        else
            gameEnded = "Player two killed the enemy hero.";
        output.addPOJO(this);
    }

    public String getGameEnded() {
        return gameEnded;
    }
}
