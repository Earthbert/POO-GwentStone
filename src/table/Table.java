package table;

import utils.UnitPos;

public class Table {
    final Row[] rows = new Row[4];

    public Table() {
        rows[0] = new Row(2, UnitPos.RANGED);
        rows[1] = new Row(2, UnitPos.CLOSE);
        rows[2] = new Row(1, UnitPos.CLOSE);
        rows[3] = new Row(1, UnitPos.RANGED);
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
        }
    }
}
