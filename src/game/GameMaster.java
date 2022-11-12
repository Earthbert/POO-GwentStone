package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    private final static GameMaster instance = new GameMaster();

    private Game currentGame;

    final Player[] player = new Player[3];

    private GameMaster() {}

    public static GameMaster getInstance() {
        return instance;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void entry(Input inputData, ArrayNode output) {
        for (GameInput gameInput : inputData.getGames()) {
            player[1] = new Player(1);
            player[2] = new Player(2);
            currentGame = new Game(gameInput, inputData.getPlayerOneDecks(), inputData.getPlayerTwoDecks());
            currentGame.play();
        }
    }
}
