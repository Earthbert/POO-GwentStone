package deck;

import cards.Card;
import cards.Minion;
import cards.environmentcards.Firestorm;
import cards.environmentcards.HeartHound;
import cards.environmentcards.Winterfell;
import cards.specialcards.Disciple;
import cards.specialcards.Miraj;
import cards.specialcards.TheCursedOne;
import cards.specialcards.TheRipper;
import fileio.CardInput;
import helpers.Errors;
import helpers.UnitProp;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final List<Card> cardsOnDeck = new LinkedList<>();
    private final List<Card> cardsOnHand = new LinkedList<>();

    private void createEnvCard(final CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Firestorm" -> cardsOnDeck.add(new Firestorm(cardInput));
            case "Heart Hound" -> cardsOnDeck.add(new HeartHound(cardInput));
            case "Winterfell" -> cardsOnDeck.add(new Winterfell(cardInput));
            default -> System.out.println(Errors.INVALID_CARD);
        }
    }

    private void createSpecialCard(final CardInput cardInput) {
        switch (cardInput.getName()) {
            case "The Ripper" -> cardsOnDeck.add(new TheRipper(cardInput));
            case "The Cursed One" -> cardsOnDeck.add(new TheCursedOne(cardInput));
            case "Miraj" -> cardsOnDeck.add(new Miraj(cardInput));
            case "Disciple" -> cardsOnDeck.add(new Disciple(cardInput));
            default -> System.out.println(Errors.INVALID_CARD);
        }
    }

    private void createStandardCard(final CardInput cardInput) {
        cardsOnDeck.add(new Minion(cardInput));
    }

    public Deck(final ArrayList<CardInput> deckInput, final int shuffleSeed) {
        for (final CardInput cardInput : deckInput) {
            switch (UnitProp.getType(cardInput.getName())) {
                case STANDARD -> createStandardCard(cardInput);
                case SPECIAL -> createSpecialCard(cardInput);
                case ENVIRONMENT -> createEnvCard(cardInput);
                default -> System.out.println(Errors.INVALID_CARD);
            }
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
