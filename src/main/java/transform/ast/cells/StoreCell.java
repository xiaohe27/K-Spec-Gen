package transform.ast.cells;

import java.util.HashMap;

/**
 * Created by xiaohe on 11/1/15.
 */
public class StoreCell extends Cell {

    private HashMap<String, String> store = new HashMap<>();

    public StoreCell(HashMap<String, String> store) {
        super(Cell.STORE);
        this.hasLeftOmission = true;
        this.hasRightOmission = true;
        this.store = store;
    }

    @Override
    public String toString() {
        String ret = "";
        if (store.isEmpty()) {
           ret = super.surroundWithTags(".Map => ?_:Map");
        } else {
            //TODO
        }

        return ret;
    }
}
