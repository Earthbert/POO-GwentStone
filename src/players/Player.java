package players;

import cards.heroes.Hero;
import deck.Deck;
import fileio.CardInput;
import helpers.UnitProp;
import lombok.SneakyThrows;

public class Player {

    private int mana;

    private int gamesWon = 0;

    private Hero hero;
    private Deck deck;

    /**
     * Prepares player for a new game.
     * @param cardInput Hero Input.
     * @param deckSelected Deck of cards.
     */
    @SneakyThrows
    public void startGame(final CardInput cardInput, final Deck deckSelected) {
        this.mana = 0;
        this.deck = deckSelected;
        this.hero = (Hero) UnitProp.getCtor(cardInput.getName()).newInstance(cardInput);
    }

    public Player() {
    }

    public final Hero getHero() {
        return hero;
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
