package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    private static final int NR_PLAYERS = 3;

    final ArrayNode output;

    private int totalGames = 0;
    private final Player[] player = new Player[NR_PLAYERS];

    public GameMaster(final ArrayNode arrayNode) {
        output = arrayNode;
    }

    /**
     * Given an Index returns player with corresponding index.
     * @param id Player Index
     * @return Player
     */
    public Player getPlayer(final int id) {
        return player[id];
    }

    /**
     * Given an Index returns enemy of that player.
     * @param id Player Index
     * @return Enemy Player
     */
    public Player getEnemyPlayerPlayer(final int id) {
        if (id == 1) {
            return player[2];
        }
        return player[1];
    }

    /**
     * Updates stats when a player wins.
     * @param playerId Winning player index.
     */
    public void winGame(final int playerId) {
        totalGames++;
        player[playerId].winGame();
    }

    public final int getTotalGames() {
        return totalGames;
    }

    /**
     * Generates games between to player.
     * Players are create at the call
     * @param inputData Input data
     */
    public void entry(final Input inputData) {
        player[1] = new Player();
        player[2] = new Player();
        for (final GameInput gameInput : inputData.getGames()) {
            final Game currentGame = new Game(gameInput, inputData.getPlayerOneDecks(),
                inputData.getPlayerTwoDecks(), this);
            currentGame.play();
        }
    }
}
