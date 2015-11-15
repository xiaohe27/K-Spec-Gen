package transform.ast.cells;

import java.util.HashMap;

/**
 * Created by xiaohe on 11/1/15.
 */
public class ObjectStoreCell extends Cell {
    private final HashMap<String, String> heap;

    public ObjectStoreCell(HashMap<String, String> heap) {
        super(Cell.OBJ_STORE);
        this.heap = heap;
    }
}