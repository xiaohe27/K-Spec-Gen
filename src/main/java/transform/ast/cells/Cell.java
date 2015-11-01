package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import transform.ast.KASTNode;

/**
 * Created by hx312 on 13/10/2015.
 */
public class Cell extends KASTNode {
    public static final String Type_K = "'k";

    public Cell(String name) {
        super(name);
    }

    public static Cell getFixedCellWithName(String cellName) {
        return new Cell(cellName) {
            @Override
            public String toString() {
                return "";
            }
        };
    }

    @Override
    public String toString() {
        return null;
    }
}
