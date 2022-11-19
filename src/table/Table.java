package table;

import cards.Minion;

import static helpers.UnitPos.CLOSE;
import static helpers.UnitPos.RANGED;

public class Table {
    public static final int NR_ROWS = 4;
    private final Row[] rows = new Row[NR_ROWS];

    public Table() {
        rows[0] = new Row(2, this);
        rows[1] = new Row(2, this);
        rows[2] = new Row(1, this);
        rows[NR_ROWS - 1] = new Row(1, this);
    }

    /**
     * Checks if a tank is placed on a side of the enemy player.
     * @param playerId player
     * @return Return true is a tank is placed on the enemy player's side and false otherwise.
     */
    public boolean isTankPlaced(final int playerId) {
        if (playerId == 1) {
            return rows[1].isTankPlaced();
        }
        return rows[2].isTankPlaced();
    }

    /**
     * Places a minion for a specific player.
     * @param minion Minion to place.
     * @param playerId On which player's side.
     * @return Return true if the placement happened, false otherwise.
     */
    public boolean placeCard(final Minion minion, final int playerId) {
        if (playerId == 2 && minion.getPosition() == RANGED) {
            return rows[0].placeCard(minion);
        } else if (playerId == 2 && minion.getPosition() == CLOSE) {
            return rows[1].placeCard(minion);
        } else if (playerId == 1 && minion.getPosition() == CLOSE) {
            return rows[2].placeCard(minion);
        } else if (playerId == 1 && minion.getPosition() == RANGED) {
            return rows[NR_ROWS - 1].placeCard(minion);
        }
        return true;
    }

    /**
     * Return a specific minion.
     * @param rowNr Row Number
     * @param cardIdx Card Index
     * @return Minion at that position, or null if no minion is preset.
     */
    public Minion getCard(final int rowNr, final int cardIdx) {
        return rows[rowNr].getCard(cardIdx);
    }

    /**
     * Calculates to which player a row belongs.
     * @param rowNr Row Number
     * @return Player Index
     */
    public int whichPlayer(final int rowNr) {
        if (rowNr > 1) {
            return 1;
        }
        return 2;
    }

    /**
     * Return a specific Row
     * @param rowNr Row Number
     * @return Specified Row
     */
    public Row getRow(final int rowNr) {
        return rows[rowNr];
    }

    /**
     * Prepares table for next turn for a player.
     * Calls prepareRow for the rows of specified player.
     *
     * @param playerId which player's side to prepare
     */
    public void prepareTable(final int playerId) {
        if (playerId == 1) {
            rows[2].prepareRow();
            rows[NR_ROWS - 1].prepareRow();
        } else {
            rows[0].prepareRow();
            rows[1].prepareRow();
        }
    }
}
