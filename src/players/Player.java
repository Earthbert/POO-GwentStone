package players;

import cards.heroes.Hero;
import deck.Deck;

public class Player {
    public final int playerId;

    private static int totalGames = 0;
    private int gamesWon = 0;

    public Hero hero;
    public Deck deck;

    public Player(int playerId) {
        this.playerId = playerId;
    }

    public void wonGame() {
        totalGames++;
        gamesWon++;
    }
}
