package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadsCell extends Cell {

    public ThreadsCell(MethodInfo methodInfo, LoopInfo loopInfo, HashMap<String, String> env) {
        super(Cell.THREADS);
        this.childrenCells.add(new ThreadCell(methodInfo, loopInfo, env));
    }

}
