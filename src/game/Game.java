package game;

import cards.heroes.*;
import deck.Deck;
import fileio.*;

import java.util.ArrayList;

public class Game {
    ArrayList<ActionsInput> actionsInput;
    private int actionIndex = 0;
    private int shuffleSeed;

    private Hero selectHero(CardInput hero) {
        return switch (hero.getName()) {
            case "Lord Royce" -> new LordRoyce(hero);
            case "Empress Thorina" -> new EmpressThorina(hero);
            case "King Mudface" -> new KingMudface(hero);
            case "General Kocioraw" -> new GeneralKocioraw(hero);
            default -> null;
        };
    }

    public Game(GameInput gameInput, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        actionsInput = gameInput.getActions();
        shuffleSeed = gameInput.getStartGame().getShuffleSeed();

        int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        GameMaster.getInstance().playerOne.deck = new Deck(playerOneDecks.getDecks().get(playerOneIndex));
        GameMaster.getInstance().playerTwo.hero = selectHero(gameInput.getStartGame().getPlayerOneHero());

        int playerTwoIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        GameMaster.getInstance().playerTwo.deck = new Deck(playerTwoDecks.getDecks().get(playerTwoIndex));
        GameMaster.getInstance().playerTwo.hero = selectHero(gameInput.getStartGame().getPlayerTwoHero());
    }
}
