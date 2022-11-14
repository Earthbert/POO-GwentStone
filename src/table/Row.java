package table;

import cards.Card;
import cards.Minion;
import utils.UnitPos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Row {
    private List<Minion> cardsOnRow = new LinkedList<>();

    public final Table table;
    public final int player;
    final UnitPos type;

    public Row(int player, UnitPos type, Table table) {
        this.player = player;
        this.type = type;
        this.table = table;
    }

    public List<Card> copyOfCards() {
        List<Card> cards = new ArrayList<>();
        for (Card card : cardsOnRow) {
            cards.add(card.clone());
        }
        return cards;
    }

    public List<Minion> getCardsOnRow() {
        return cardsOnRow;
    }

    public int getNrOfCards () {
        return cardsOnRow.size();
    }

    public boolean isTankPlaced () {
        for (Minion m : cardsOnRow) {
            if (m.isTank())
                return true;
        }
        return false;
    }

    boolean placeCard (Minion minion) {
        if (cardsOnRow.size() >= 5)
            return false;
        cardsOnRow.add(minion);
        minion.placedCard(this);
        return true;
    }

    Minion getCard (int CardIdx) {
        if (CardIdx >= cardsOnRow.size())
            return null;
        return cardsOnRow.get(CardIdx);
    }

    public void removeCard(Minion minion) {
        cardsOnRow.remove(minion);
    }

    /**
     * Prepare row for next turn.
     * Calls prepareCard.
     */
    public void prepareRow() {
        for (Minion minion : cardsOnRow) {
            minion.prepareCard();
        }
    }
}