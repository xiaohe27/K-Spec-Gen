package transform.ast.cells;

import java.util.HashMap;

/**
 * Created by xiaohe on 11/1/15.
 */
public class StoreCell extends Cell {
    private HashMap<String, String> store;

    public StoreCell(HashMap<String, String> store) {
        super(Cell.STORE);
        this.store = store;
    }


}
