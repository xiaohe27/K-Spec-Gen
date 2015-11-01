package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import transform.ast.cells.Cell;
import transform.ast.cells.ThreadsCell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KRule extends KASTNode {
    private ArrayList<KCondition> preConds;
    private ArrayList<KCondition> postConds;
    private String retVal;
    private ArrayList<Cell> cells;

    private HashMap<String, String> stack; //stack maps vars to vals
    private HashMap<String, String> heap;  //heap maps addr to vals

    public KRule(MethodInfo methodInfo) {
        this(methodInfo, null);
    }

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo) {
        super("'KRule");
        this.preConds.addAll(extractAllPreCond(methodInfo.getPreCondList()));
        this.postConds.addAll(extractAllPostCond(methodInfo.getPostCondList()));
        this.retVal = methodInfo.getRetVal();
        this.cells = constructCells(methodInfo, loopInfo);
        initStackAndHeap(methodInfo, loopInfo);
    }

    private void initStackAndHeap(MethodInfo methodInfo, LoopInfo loopInfo) {
        this.stack = new HashMap<>();
        this.heap = new HashMap<>();
    }

    private ArrayList<Cell> constructCells(MethodInfo methodInfo, LoopInfo loopInfo) {
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new ThreadsCell("Threads", methodInfo, loopInfo));


        return cells;
    }


    private Collection<? extends KCondition> extractAllPostCond(ArrayList<Expression> postCondList) {
        return null;
    }

    private Collection<? extends KCondition> extractAllPreCond(ArrayList<Expression> preCondList) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
