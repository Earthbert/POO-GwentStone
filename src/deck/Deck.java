package deck;

import cards.Card;
import cards.Minion;
import cards.specialcards.*;
import cards.environmentcards.*;
import fileio.CardInput;
import utils.UnitProp;

import java.util.*;

public class Deck {
    private List<Card> cardOnDeck = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();

    private void createEnvCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Firestorm" -> cardOnDeck.add(new Firestorm(cardInput));
            case "Heart Hound" -> cardOnDeck.add(new HeartHound(cardInput));
            case "Winterfell" -> cardOnDeck.add(new Winterfell(cardInput));
        }
    }

    private void createSpecialCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "The Ripper" -> cardOnDeck.add(new TheRipper(cardInput));
            case "The Cursed One" -> cardOnDeck.add(new TheCursedOne(cardInput));
            case "Miraj" -> cardOnDeck.add(new Miraj(cardInput));
            case "Disciple" -> cardOnDeck.add(new Disciple(cardInput));
        }
    }

    private void createStandardCard(CardInput cardInput) {
        cardOnDeck.add(new Minion(cardInput));
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
        Collections.shuffle(cardOnDeck, rnd);
    }

    public List<Card> getCardOnDeck() {
        return cardOnDeck;
    }

    public void setCardOnDeck(List<Card> cardOnDeck) {
        this.cardOnDeck = cardOnDeck;
    }

    public void prepareDeck() {
        if (!cardOnDeck.isEmpty())
            hand.add(cardOnDeck.remove(0));
    }
}
