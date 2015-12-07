package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadsCell extends Cell {
    public ThreadsCell(MethodInfo methodInfo, LoopInfo loopInfo, HashMap<SimpleName, Integer> env) {
        super(Cell.THREADS);
        ThreadCell singleThreadCell = new ThreadCell(methodInfo, loopInfo, env);
        this.childrenCells.add(singleThreadCell);
    }
}
