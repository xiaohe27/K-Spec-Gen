package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadCell extends Cell {
    private KCell kCell;
    private EnvCell envCell;
    private HoldsCell holdsCell;

    public ThreadCell(String name, MethodInfo methodInfo, LoopInfo loopInfo) {
        super(name);
        this.kCell = new KCell("k", methodInfo, loopInfo);
    }
}
