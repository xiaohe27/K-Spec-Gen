package transform.ast.cells;

import transform.ast.rewrite.KRewriteObj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by xiaohe on 11/1/15.
 */
public class StoreCell extends Cell {
    private Collection<Integer> definedLoc = new ArrayList<>();
    private HashMap<Integer, KRewriteObj> store;

    public StoreCell(HashMap<Integer, KRewriteObj> store) {
        super(Cell.STORE);
        this.hasLeftOmission = true;
        this.hasRightOmission = true;
        this.store = store;
    }

    public StoreCell setDefinedLoc(Collection<Integer> definedLoc) {
        this.definedLoc = definedLoc;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( .Map => ?_:Map )");

        sb.append("\n");

        if (this.definedLoc != null)
            this.store.forEach((loc, val) -> {
                if (this.definedLoc.contains(loc))
                    sb.append("P" + loc + " |-> " + val + "\n");
            });

        sb.deleteCharAt(sb.lastIndexOf("\n"));

        return super.surroundWithTags(sb.toString());
    }
}
