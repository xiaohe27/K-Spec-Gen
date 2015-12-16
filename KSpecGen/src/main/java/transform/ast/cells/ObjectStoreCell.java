package transform.ast.cells;

import transform.utils.Utils;

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

        final boolean[] hasRewrite = {false};

        StringBuilder sb = new StringBuilder();

        this.objStores.forEach(obj -> {
            if (obj.contains("=>"))
                hasRewrite[0] = true;

            sb.append(Utils.addBrackets(obj.trim()) + "\n");
        });

        if (hasRewrite[0])
            sb.append(" (.Bag => ?_:Bag) ");
        return super.surroundWithTags(sb.toString());
    }


}
