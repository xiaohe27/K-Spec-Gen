package transform.ast.cells;

import transform.ast.rewrite.KRewriteObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohe on 11/1/15.
 */
public class ObjectStoreCell extends Cell {
    private final List<KRewriteObj> objStores = new ArrayList<>();

    public ObjectStoreCell(List<KRewriteObj> objStores0) {
        super(Cell.OBJ_STORE);
        if (objStores0 != null)
            this.objStores.addAll(objStores0);
    }

    @Override
    public String toString() {
        if (this.objStores.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        this.objStores.forEach(kRewriteObj -> {
            sb.append(kRewriteObj.toString() + "\n");
        });

        sb.append(" (.Bag => ?_:Bag) ");
        return super.surroundWithTags(sb.toString());
    }
}
