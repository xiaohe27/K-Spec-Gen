package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

/**
 * Created by hx312 on 31/10/2015.
 */
public class KCell extends Cell {
    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    public KCell(String name, MethodInfo methodInfo, LoopInfo loopInfo) {
        super(name);
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;
    }

}
