package table;

import cards.Minion;
import consts.UnitType;

import java.util.LinkedList;
import java.util.List;

public class Row {
    private List<Minion> cardOnRow = new LinkedList<>();

    final int player;
    final UnitType type;

    public Row(int player, UnitType type) {
        this.player = player;
        this.type = type;
    }

    public int getNrOfCards () {
        return cardOnRow.size();
    }
}