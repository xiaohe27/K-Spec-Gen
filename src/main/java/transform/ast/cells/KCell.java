package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 31/10/2015.
 */
public class KCell extends Cell {
    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    private final String className;
    private final String methodName;
    private final ArrayList<String> methArgs = new ArrayList<>();
    private String retVal;

    public KCell(MethodInfo methodInfo, LoopInfo loopInfo) {
        super(Cell.K);
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;

        init();
    }

    private void init() {
        this.className = this.methodInfo.
    }

}
