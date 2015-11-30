package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;

import java.util.HashMap;

/**
 * Created by xiaohe on 11/1/15.
 */
public class StoreCell extends Cell {

    private HashMap<Integer, SimpleName> store;

    public StoreCell(HashMap<Integer, SimpleName> store) {
        super(Cell.STORE);
        this.hasLeftOmission = true;
        this.hasRightOmission = true;
        this.store = store;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".Map => ?_:Map");

        sb.append("\n");

        this.store.forEach((loc, val) -> {
            sb.append(loc + " |-> " + val + "\n");
        });

        sb.deleteCharAt(sb.lastIndexOf("\n"));

        return super.surroundWithTags(sb.toString());
    }
}
