package players;

import cards.Hero;
import deck.Deck;

public class Player {
    private static int totalGames = 0;
    private int gamesWon = 0;

    public Hero hero;
    public Deck deck;

    public void wonGame() {
        totalGames++;
        gamesWon++;
    }
}
