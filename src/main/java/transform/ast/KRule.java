package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import transform.ast.cells.Cell;
import transform.ast.cells.StoreCell;
import transform.ast.cells.StoreMetaDataCell;
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

    private HashMap<String, String> env; //env maps vars to vals
    private HashMap<String, String> store;  //store maps addr to primitive vals?
    private HashMap<String, String> objectStore; //obj store maps addr to obj?

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
        this.env = new HashMap<>();
        this.store = new HashMap<>();
        this.objectStore = new HashMap<>();

        /* TODO */
        throw new NotImplementedException();
    }

    private ArrayList<Cell> constructCells(MethodInfo methodInfo, LoopInfo loopInfo) {
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new ThreadsCell("Threads", methodInfo, loopInfo));

        cells.add(Cell.getFixedCellWithName(Cell.CLASSES));
        cells.add(Cell.getFixedCellWithName(Cell.NumOfClassesToUnfold));
        cells.add(Cell.getFixedCellWithName(Cell.PROGRAM));
        cells.add(Cell.getFixedCellWithName(Cell.GlobalPhase));

        cells.add(new StoreCell(this.store)); //store cell
        cells.add(new StoreMetaDataCell(this.store.keySet())); //store meta data cell

        cells.add(Cell.getFixedCellWithName(Cell.BUSY));
        cells.add(Cell.getFixedCellWithName(Cell.NEXT_LOC));
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
