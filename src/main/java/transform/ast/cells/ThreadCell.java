package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadCell extends Cell {
    protected KCell kCell;

    public ThreadCell(MethodInfo methodInfo, LoopInfo loopInfo, HashMap<SimpleName, Integer> env) {
        super(Cell.THREAD);
        this.kCell = new KCell(methodInfo, loopInfo);
        Cell envCell = new EnvCell(env);
        Cell holdsCell = Cell.getFixedCellWithName(Cell.HOLDS);

        this.childrenCells.add(this.kCell);
        if (env != null)
            this.childrenCells.add(envCell);

        this.childrenCells.add(holdsCell);
    }
}
