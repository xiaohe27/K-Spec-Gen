package transform.ast.cells;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 31/10/2015.
 */
public class ThreadsCell extends Cell {
    private ArrayList<ThreadCell> threadCells;

    public ThreadsCell(String name, MethodInfo methodInfo, LoopInfo loopInfo) {
        super(name);
        this.threadCells = new ArrayList<>();
        this.threadCells.add(new ThreadCell("FirstThread", methodInfo, loopInfo));
    }
}
