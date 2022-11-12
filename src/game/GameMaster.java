package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    ArrayNode output;

    private final Player[] player = new Player[3];

    public Player getPlayer(int id) {
        return player[id];
    }

    private Game currentGame;
    public Game getCurrentGame() {
        return currentGame;
    }

    public void entry(Input inputData, ArrayNode output) {
        player[1] = new Player(1);
        player[2] = new Player(2);
        for (GameInput gameInput : inputData.getGames()) {
            this.output = output;
            currentGame = new Game(gameInput, inputData.getPlayerOneDecks(), inputData.getPlayerTwoDecks(), this);
            currentGame.play();
        }
    }
}
