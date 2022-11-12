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

    public int getNrOfCards () {
        return cardsOnRow.size();
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