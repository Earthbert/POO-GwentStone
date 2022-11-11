package table;

import utils.UnitPos;

public class Table {
    final Row[] rows = new Row[4];

    private Table() {
        rows[0] = new Row(2, UnitPos.RANGED);
        rows[1] = new Row(2, UnitPos.CLOSE);
        rows[2] = new Row(1, UnitPos.CLOSE);
        rows[3] = new Row(1, UnitPos.RANGED);
    }
}
