package deck;

import cards.Card;
import fileio.CardInput;
import helpers.UnitProp;
import lombok.SneakyThrows;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final List<Card> cardsOnDeck = new LinkedList<>();
    private final List<Card> cardsOnHand = new LinkedList<>();

    @SneakyThrows
    public Deck(final ArrayList<CardInput> deckInput, final int shuffleSeed) {
        for (final CardInput cardInput : deckInput) {
            final Card card = (Card) UnitProp.getCtor(cardInput.getName()).newInstance(cardInput);
            cardsOnDeck.add(card);
        }
        final Random rnd = new Random(shuffleSeed);
        Collections.shuffle(cardsOnDeck, rnd);
    }

     /**
     * Return cards from the hand of a player.
     * @param cardIdx Card Index
     * @return Card at index.
     */
    public final Card getCardFromHand(final int cardIdx) {
        return cardsOnHand.get(cardIdx);
    }

    /**
     * Removes a card from hand.
     * It is called when that card has been used/placed.
     * @param cardIdx Card Index.
     */
    public void usedCard(final int cardIdx) {
        cardsOnHand.remove(cardIdx);
    }

    public final List<Card> getCardsOnDeck() {
        return cardsOnDeck;
    }

    public final List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    /**
     * Prepare player's deck at the start of the round.
     * Takes a card from the deck and puts it in the hand.
     */
    public void prepareDeck() {
        if (!cardsOnDeck.isEmpty()) {
            cardsOnHand.add(cardsOnDeck.remove(0));
        }
    }
}
