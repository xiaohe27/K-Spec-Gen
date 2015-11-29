package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadsCell extends Cell {
    private KCell singleKCell;

    public ThreadsCell(MethodInfo methodInfo, LoopInfo loopInfo, HashMap<String, Integer> env) {
        super(Cell.THREADS);
        ThreadCell singleThreadCell = new ThreadCell(methodInfo, loopInfo, env);
        this.childrenCells.add(singleThreadCell);
        this.singleKCell = singleThreadCell.kCell;
    }

    public KCell getSingleKCell() {
        return singleKCell;
    }
}
