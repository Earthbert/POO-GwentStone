package table;

import cards.Minion;
import utils.UnitPos;

import static utils.UnitPos.CLOSE;
import static utils.UnitPos.RANGED;

public class Table {

    final Row[] rows = new Row[4];

    public Table() {
        rows[0] = new Row(2, RANGED, this);
        rows[1] = new Row(2, UnitPos.CLOSE, this);
        rows[2] = new Row(1, UnitPos.CLOSE, this);
        rows[3] = new Row(1, RANGED, this);
    }

    public boolean canAttack(int playerId) {
        if (playerId == 1) {
            return !(rows[1].isTankPlaced());
        }
        return !(rows[2].isTankPlaced());
    }

    public boolean placeCard (Minion minion, int playerId) {
        if (playerId == 2 && minion.getPosition() == RANGED) {
            return rows[0].placeCard(minion);
        } else if (playerId == 2 && minion.getPosition() == CLOSE) {
            return rows[1].placeCard(minion);
        } else if (playerId == 1 && minion.getPosition() == CLOSE) {
            return rows[2].placeCard(minion);
        } else if (playerId == 1 && minion.getPosition() == RANGED) {
            return rows[3].placeCard(minion);
        }
        return true;
    }

    public Minion getCard (int rowNr, int CardIdx) {
        return rows[rowNr].getCard(CardIdx);
    }

    public int whichPlayer(int rowNr) {
        if (rowNr > 1)
            return 1;
        return 2;
    }

    public Row getRow(int rowNr) {
        return rows[rowNr];
    }

    /**
     * Prepares table for next turn for a player.
     * Calls prepareRow for the rows of specified player.
     *
     * @param playerId which player's side to prepare
     */
    public void prepareTable(int playerId) {
        if (playerId == 1) {
            rows[2].prepareRow();
            rows[3].prepareRow();
        } else {
            rows[0].prepareRow();
            rows[1].prepareRow();
        }
    }
}
