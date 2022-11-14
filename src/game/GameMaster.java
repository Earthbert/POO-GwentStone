package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    ArrayNode output;

    private int totalGames = 0;
    private final Player[] player = new Player[3];

    public Player getPlayer(int id) {
        return player[id];
    }

    public Player getOtherPlayer(int id) {
        if (player[id].playerId == 1)
            return player[2];
        return player[1];
    }

    public void winGame(int playerId) {
        totalGames++;
        player[playerId].winGame();
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void entry(Input inputData, ArrayNode output) {
        player[1] = new Player(1);
        player[2] = new Player(2);
        this.output = output;
        for (GameInput gameInput : inputData.getGames()) {
            Game currentGame = new Game(gameInput, inputData.getPlayerOneDecks(), inputData.getPlayerTwoDecks(), this);
            currentGame.play();
        }
    }
}
