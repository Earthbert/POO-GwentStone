package players;

import cards.heroes.EmpressThorina;
import cards.heroes.LordRoyce;
import cards.heroes.KingMudface;
import cards.heroes.GeneralKocioraw;
import cards.heroes.Hero;
import deck.Deck;
import fileio.CardInput;

public class Player {
    private final int playerId;

    private int mana;

    private int gamesWon = 0;

    private Hero hero;
    private Deck deck;

    /**
     * Prepares player for a new game.
     * @param cardInput Hero Input.
     * @param deckSelected Deck of cards.
     */
    public void startGame(final CardInput cardInput, final Deck deckSelected) {
        this.mana = 0;
        this.deck = deckSelected;
        this.hero = switch (cardInput.getName()) {
            case "Lord Royce" -> new LordRoyce(cardInput);
            case "Empress Thorina" -> new EmpressThorina(cardInput);
            case "King Mudface" -> new KingMudface(cardInput);
            case "General Kocioraw" -> new GeneralKocioraw(cardInput);
            default -> null;
        };
    }

    public Player(final int playerId) {
        this.playerId = playerId;
    }

    public final Hero getHero() {
        return hero;
    }

    public final void setHero(final Hero hero) {
        this.hero = hero;
    }

    public final Deck getDeck() {
        return deck;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Update gamesWon field when player wins a game.
     */
    public void winGame() {
        gamesWon++;
    }

    public final int getGamesWon() {
        return gamesWon;
    }

    /**
     * Prepare player at the start of a round.
     *
     * @param manaGain the mana gain
     */
    public void preparePlayer(final int manaGain) {
        mana += manaGain;
        deck.prepareDeck();
    }
}
