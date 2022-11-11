package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;
import players.Player;

public class GameMaster {
    private final static GameMaster instance = new GameMaster();

    Player playerOne = new Player(1);
    Player playerTwo = new Player(2);

    private GameMaster() {}

    public static GameMaster getInstance() {
        return instance;
    }

    public void entryProgram (Input inputData, ArrayNode output) {
        for (GameInput gameInput : inputData.getGames()) {
            Game game = new Game(gameInput, inputData.getPlayerOneDecks(), inputData.getPlayerTwoDecks());
        }
    }
}
