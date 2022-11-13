package players;

import cards.heroes.*;
import deck.Deck;
import fileio.CardInput;

public class Player {
    public final int playerId;

    private int mana;

    private int totalGames = 0;
    private int gamesWon = 0;

    private Hero hero;
    private Deck deck;

    public void startGame(CardInput hero, Deck deck) {
        this.mana = 0;
        this.deck = deck;
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

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void winGame() {
        totalGames++;
        gamesWon++;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Prepare player at the start of a round.
     *
     * @param manaGain the mana gain
     */
    public void preparePlayer(int manaGain) {
        mana += manaGain;
        deck.prepareDeck();
    }
}
