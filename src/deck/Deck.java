package deck;

import cards.Card;
import cards.Minion;
import cards.specialcards.*;
import cards.environmentcards.*;
import fileio.CardInput;
import utils.UnitProp;

import java.util.*;

public class Deck {
    private List<Card> cardsOnDeck = new LinkedList<>();
    private List<Card> cardsOnHand = new LinkedList<>();

    private void createEnvCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Firestorm" -> cardsOnDeck.add(new Firestorm(cardInput));
            case "Heart Hound" -> cardsOnDeck.add(new HeartHound(cardInput));
            case "Winterfell" -> cardsOnDeck.add(new Winterfell(cardInput));
        }
    }

    private void createSpecialCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "The Ripper" -> cardsOnDeck.add(new TheRipper(cardInput));
            case "The Cursed One" -> cardsOnDeck.add(new TheCursedOne(cardInput));
            case "Miraj" -> cardsOnDeck.add(new Miraj(cardInput));
            case "Disciple" -> cardsOnDeck.add(new Disciple(cardInput));
        }
    }

    private void createStandardCard(CardInput cardInput) {
        cardsOnDeck.add(new Minion(cardInput));
    }

    public Deck(ArrayList<CardInput> deckInput, int shuffleSeed) {
        for (CardInput cardInput : deckInput) {
            switch (UnitProp.getType(cardInput.getName())) {
                case "Standard" -> createStandardCard(cardInput);
                case "Special" -> createSpecialCard(cardInput);
                case "Environment" -> createEnvCard(cardInput);
                case "none" -> System.out.println("Invalid Card");
            }

        }
        Random rnd = new Random(shuffleSeed);
        Collections.shuffle(cardsOnDeck, rnd);
    }

    public List<Card> getCardOnDeck() {
        return cardsOnDeck;
    }

    public Card getCardFromHand(int cardIdx) {
        return cardsOnHand.get(cardIdx);
    }

    public void usedCard(int cardIdx) {
        cardsOnHand.remove(cardIdx);
    }

    public List<Card> getCardsOnDeck() {
        return cardsOnDeck;
    }

    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void prepareDeck() {
        if (!cardsOnDeck.isEmpty())
            cardsOnHand.add(cardsOnDeck.remove(0));
    }
}
