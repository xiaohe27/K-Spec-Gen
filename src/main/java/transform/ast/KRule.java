package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import transform.ast.cells.*;
import transform.utils.ConstraintGen;
import transform.utils.TypeMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KRule extends KASTNode {
    private ArrayList<KCondition> preConds = new ArrayList<>();
    private ArrayList<KCondition> postConds = new ArrayList<>();
    private String retVal;
    private ArrayList<Cell> cells = new ArrayList<>();

    private HashMap<String, String> env = new HashMap<>(); //env maps vars to vals
    private HashMap<String, String> store = new HashMap<>();  //store maps addr to primitive vals?
    private HashMap<String, String> objectStore = new HashMap<>(); //obj store maps addr to obj?

    public KRule(MethodInfo methodInfo) {
        this(methodInfo, null);
    }

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo) {
        super("'KRule");

//        initStackAndHeap(methodInfo, loopInfo);

        this.preConds.addAll(extractAllPreCond(methodInfo.getPreCondList(), methodInfo.getFormalParams()));
        this.postConds.addAll(extractAllPostCond(methodInfo.getPostCondList()));
        this.retVal = methodInfo.getRetVal();
        this.cells = constructCells(methodInfo, loopInfo);
    }

    private void initStackAndHeap(MethodInfo methodInfo, LoopInfo loopInfo) {
        /* TODO */
        throw new NotImplementedException();
    }

    private ArrayList<Cell> constructCells(MethodInfo methodInfo, LoopInfo loopInfo) {
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new ThreadsCell(methodInfo, loopInfo, this.env));

        cells.add(Cell.getFixedCellWithName(Cell.CLASSES));
        cells.add(Cell.getFixedCellWithName(Cell.NumOfClassesToUnfold));
        cells.add(Cell.getFixedCellWithName(Cell.PROGRAM));
        cells.add(Cell.getFixedCellWithName(Cell.GlobalPhase));

        cells.add(new StoreCell(this.store)); //store cell
        cells.add(new StoreMetaDataCell(this.store.keySet())); //store meta data cell

        cells.add(Cell.getFixedCellWithName(Cell.BUSY));
        cells.add(Cell.getFixedCellWithName(Cell.NEXT_LOC));
        cells.add(new ObjectStoreCell(this.objectStore));
        return cells;
    }


    private Collection<? extends KCondition> extractAllPostCond(ArrayList<Expression> postCondList) {
        Collection<? extends KCondition> allPostCond = new ArrayList<>();
        return allPostCond;
    }

    private Collection<KCondition> extractAllPreCond(ArrayList<Expression> preCondList,
                                                     ArrayList<SingleVariableDeclaration> formalParams) {
        Collection<KCondition> allPreCond = new ArrayList<>();
        //default params range conditions
        formalParams.forEach(varDecl -> allPreCond.add(new KCondition(ConstraintGen
                .genRangeConstraint4Type(varDecl.getType().toString(),
                        TypeMapping.freshVar(varDecl.getName().toString(), varDecl.getType().isPrimitiveType())))));

        //TODO: constraints generated from expressions.

        return allPreCond;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rule\n");
        this.cells.forEach(cell -> sb.append(cell.toString()));

        if (!this.preConds.isEmpty()) {
            sb.append("requires ");
            this.preConds.forEach(preCond ->
                    sb.append(preCond.toString() +
                                    ((this.preConds.get(this.preConds.size() - 1).equals
                                            (preCond)) ? "\n" : " andBool ")
                    ));
        }

        if (!this.postConds.isEmpty()) {
            sb.append("ensures ");
            this.postConds.forEach(postCond ->
                    sb.append(postCond.toString() +
                                    ((this.postConds.get(this.postConds.size() - 1).equals
                                            (postCond)) ? "\n" : " andBool ")
                    ));
        }

        return sb.toString();
    }

    /**
     * Test the KRule's toString().
     *
     * @param args
     */
    public static void main(String[] args) {
//        KRule kRule = new KRule(new MethodInfo("test", 0, 10, "fakePreAndPostCond"));
//        System.out.println("KRule looks like:\n");
//        System.out.println(kRule.toString());
    }
}
