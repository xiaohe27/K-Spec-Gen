package transform.ast.cells;

/**
 * Created by xiaohe on 11/1/15.
 */
public class NextLocCell extends Cell {
    private String locVar;

    public NextLocCell(String locVar) {
        super(NEXT_LOC);
        this.locVar = locVar;
    }
}
