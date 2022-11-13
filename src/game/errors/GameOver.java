package game.errors;

public class GameOver {
    private final String gameEnded;

     public GameOver(int playerId) {
        if (playerId == 1)
            gameEnded = "Player one killed the enemy hero.";
        else
            gameEnded = "Player two killed the enemy hero.";
    }

    public String getGameEnded() {
        return gameEnded;
    }
}
