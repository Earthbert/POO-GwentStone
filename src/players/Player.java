package players;

import cards.heroes.*;
import deck.Deck;
import fileio.CardInput;

public class Player {
    public final int playerId;

    private static int totalGames = 0;
    private int gamesWon = 0;

    public Hero hero;
    public Deck deck;

    public void selectHero(CardInput hero) {
        this.hero = switch (hero.getName()) {
            case "Lord Royce" -> new LordRoyce(hero);
            case "Empress Thorina" -> new EmpressThorina(hero);
            case "King Mudface" -> new KingMudface(hero);
            case "General Kocioraw" -> new GeneralKocioraw(hero);
            default -> null;
        };
    }

    public Player(int playerId) {
        this.playerId = playerId;
    }

    public void wonGame() {
        totalGames++;
        gamesWon++;
    }
}
