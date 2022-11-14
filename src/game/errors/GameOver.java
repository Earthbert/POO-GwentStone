package game.errors;

import helpers.Errors;

public class GameOver {
    private final String gameEnded;

     public GameOver(final int playerId) {
        if (playerId == 1) {
            gameEnded = Errors.PLAYER_ONE_KILL;
        } else {
            gameEnded = Errors.PLAYER_TWO_KILL;
        }
    }

    public final String getGameEnded() {
        return gameEnded;
    }
}
