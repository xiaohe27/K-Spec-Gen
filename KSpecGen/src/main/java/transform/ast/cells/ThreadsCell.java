package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadsCell extends Cell {
    public ThreadsCell(MethodInfo methodInfo, LoopInfo loopInfo,
                       HashMap<SimpleName, Integer> env,
                       List<String> objStore) {
        super(Cell.THREADS);
        ThreadCell singleThreadCell = new ThreadCell(methodInfo, loopInfo, env, objStore);
        this.childrenCells.add(singleThreadCell);
    }
}
