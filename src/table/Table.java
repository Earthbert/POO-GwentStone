package table;

public class Table {
    private final static Table Instance = new Table();

    final Row[] rows = new Row[4];

    private Table() {
        rows[0] = new Row(2, UnitType.RANGED);
        rows[1] = new Row(2, UnitType.CLOSE);
        rows[2] = new Row(1, UnitType.CLOSE);
        rows[3] = new Row(1, UnitType.RANGED);
    }

    public static Table getTable() {
        return Instance;
    }
}
