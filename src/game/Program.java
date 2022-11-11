package game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import deck.Deck;
import fileio.GameInput;
import fileio.Input;
import players.Player;

import java.util.ArrayList;

public class Program {
    static Player playerOne = new Player();
    static Player playerTwo = new Player();

    public static void entryProgram (Input inputData, ArrayNode output) {
        for (GameInput gameInput : inputData.getGames()) {
            Game game = new Game(gameInput, inputData.getPlayerOneDecks(), inputData.getPlayerTwoDecks());
        }
    }
}
