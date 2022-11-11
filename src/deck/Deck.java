package deck;

import cards.*;
import cards.specialcards.*;
import consts.ConstLists;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class Deck {
    private List<Card> cardList = new LinkedList<>();

    public Deck(ArrayList<CardInput> deckInput) {
        for (CardInput cardInput : deckInput) {
            switch (cardInput.getName()) {
                case "The Ripper":
                    cardList.add(new TheRipper());
                    break;
                case "The Cursed One":
                    cardList.add(new TheCursedOne());
                    break;
                case "Miraj":
                    cardList.add(new Miraj());
                    break;
                case "Disciple":
                    cardList.add(new Disciple());
                    break;
                default:

                    for (String cardName : ConstLists.StandardCards) {
                        if (cardInput.getName().equals(cardName)) {
                            cardList.add(new Minion());
                            break;
                        }
                    }
            }
        }
    }
}
