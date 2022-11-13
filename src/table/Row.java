package table;

import cards.Minion;
import utils.UnitPos;

import java.util.LinkedList;
import java.util.List;

public class Row {
    private List<Minion> cardsOnRow = new LinkedList<>();

    final int player;
    final UnitPos type;

    public Row(int player, UnitPos type) {
        this.player = player;
        this.type = type;
    }

    public List<Minion> getCardsOnRow() {
        return cardsOnRow;
    }

    public int getNrOfCards () {
        return cardsOnRow.size();
    }

    public boolean isTankPlaced () {
        for (Minion minion : cardsOnRow) {
            if (minion.isTank())
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