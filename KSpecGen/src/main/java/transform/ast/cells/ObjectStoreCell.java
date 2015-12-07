package transform.ast.cells;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohe on 11/1/15.
 */
public class ObjectStoreCell extends Cell {
    private final List<String> objStores = new ArrayList<>();

    public ObjectStoreCell(List<String> objStores0) {
        super(Cell.OBJ_STORE);
        if (objStores0 != null)
            this.objStores.addAll(objStores0);
        this.hasLeftOmission = true;
        this.hasRightOmission = true;
    }

    @Override
    public String toString() {
        if (this.objStores.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        this.objStores.forEach(obj -> {
            sb.append(obj.toString() + "\n");
        });

        sb.append(" (.Bag => ?_:Bag) ");
        return super.surroundWithTags(sb.toString());
    }
}
