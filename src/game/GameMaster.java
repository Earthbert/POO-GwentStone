package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    private static final int NR_PLAYERS = 3;

    ArrayNode output;

    private int totalGames = 0;
    private final Player[] player = new Player[NR_PLAYERS];

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
     * @param arrayNode where to print output
     */
    public void entry(final Input inputData, final ArrayNode arrayNode) {
        player[1] = new Player(1);
        player[2] = new Player(2);
        this.output = arrayNode;
        for (final GameInput gameInput : inputData.getGames()) {
            final Game currentGame = new Game(gameInput, inputData.getPlayerOneDecks(),
                inputData.getPlayerTwoDecks(), this);
            currentGame.play();
        }
    }
}
