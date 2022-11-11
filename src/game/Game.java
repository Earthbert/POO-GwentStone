package game;

import deck.Deck;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;

public class Game {
    ArrayList<ActionsInput> actionsInput;
    private int actionIndex = 0;
    private int shuffleSeed;

    public Game(GameInput gameInput, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        actionsInput = gameInput.getActions();
        shuffleSeed = gameInput.getStartGame().getShuffleSeed();

        int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        Program.playerOne.deck = new Deck(playerOneDecks.getDecks().get(playerOneIndex));

        int playerTwoIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        Program.playerOne.deck = new Deck(playerOneDecks.getDecks().get(playerTwoIndex));
    }
}
