package game;

import deck.Deck;
import fileio.*;

import java.util.ArrayList;

public class Game {
    private final ArrayList<ActionsInput> commands;
    private int cmdInx = 0;
    private final int startingPlayer;
    private final int secondPlayer;

    public Game(GameInput gameInput, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        commands = gameInput.getActions();
        int shuffleSeed = gameInput.getStartGame().getShuffleSeed();
        startingPlayer = gameInput.getStartGame().getStartingPlayer();
        if (startingPlayer == 1)
            secondPlayer = 2;
        else
            secondPlayer = 1;

        int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        GameMaster.getInstance().player[1].deck = new Deck(playerOneDecks.getDecks().get(playerOneIndex), shuffleSeed);
        GameMaster.getInstance().player[1].selectHero(gameInput.getStartGame().getPlayerOneHero());

        int playerTwoIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        GameMaster.getInstance().player[2].deck = new Deck(playerTwoDecks.getDecks().get(playerTwoIndex), shuffleSeed);
        GameMaster.getInstance().player[2].selectHero(gameInput.getStartGame().getPlayerTwoHero());
    }

    void playTurn(int playerId) {
        while (cmdInx < commands.size()) {
            ActionsInput commad = commands.get(cmdInx);
            cmdInx++;
        }
    }

    void startGame() {
        while (true) {
            playTurn(startingPlayer);
            playTurn(secondPlayer);

        }
    }
}
