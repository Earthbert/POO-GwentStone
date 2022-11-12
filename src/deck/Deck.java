package deck;

import cards.Card;
import cards.Minion;
import cards.specialcards.*;
import cards.environmentcards.*;
import fileio.CardInput;
import utils.UnitProp;

import java.util.*;

public class Deck {
    private List<Card> cardList = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();

    private void createEnvCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Firestorm" -> cardList.add(new Firestorm(cardInput));
            case "Heart Hound" -> cardList.add(new HeartHound(cardInput));
            case "Winterfell" -> cardList.add(new Winterfell(cardInput));
        }
    }

    private void createSpecialCard(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "The Ripper" -> cardList.add(new TheRipper(cardInput));
            case "The Cursed One" -> cardList.add(new TheCursedOne(cardInput));
            case "Miraj" -> cardList.add(new Miraj(cardInput));
            case "Disciple" -> cardList.add(new Disciple(cardInput));
        }
    }

    private void createStandardCard(CardInput cardInput) {
        cardList.add(new Minion(cardInput));
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
        Collections.shuffle(cardList, rnd);
    }
}
