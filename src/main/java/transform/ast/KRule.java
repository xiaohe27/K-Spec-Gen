package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import parser.ast_visitor.LIVisitor;
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

    private HashMap<SimpleName, Integer> env = new HashMap<>(); //env maps vars to locations
    private HashMap<Integer, Expression> store = new HashMap<>();  //store maps addr to primitive vals?
    private HashMap<String, String> objectStore = new HashMap<>(); //obj store maps addr to obj?

    public KRule(MethodInfo methodInfo) {
        this(methodInfo, null);
    }

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo) {
        super("'KRule");
        this.preConds.addAll(extractAllPreCond(methodInfo.getPreCondList(), methodInfo.getFormalParams()));
        this.postConds.addAll(extractAllPostCond(methodInfo.getPostCondList(), methodInfo.getFormalParams()));
        this.retVal = methodInfo.getExpectedRetVal();
        this.cells = constructCells(methodInfo, loopInfo);

        if (loopInfo != null) {
            rewrite(loopInfo);
        }
    }

    private void rewrite(LoopInfo loopInfo) {
        loopInfo.getLIStream().forEach(
                liExp -> {
                    LIVisitor liVisitor = new LIVisitor();
                    liExp.accept(liVisitor);
                }
        );

        //TODO
    }


    private ArrayList<Cell> constructCells(MethodInfo methodInfo, LoopInfo loopInfo) {
        ArrayList<Cell> cells = new ArrayList<>();
        ThreadsCell threadsCell = new ThreadsCell(methodInfo, loopInfo, this.env);
        cells.add(threadsCell);

        //Update the env and store
        threadsCell.getSingleKCell().updateEnvAndStore(this.env, this.store);

        cells.add(Cell.getFixedCellWithName(Cell.CLASSES));
        cells.add(Cell.getFixedCellWithName(Cell.NumOfClassesToUnfold));
        cells.add(Cell.getFixedCellWithName(Cell.PROGRAM));
        cells.add(Cell.getFixedCellWithName(Cell.GlobalPhase));

        cells.add(new StoreCell(this.store)); //store cell
        cells.add(new StoreMetaDataCell(this.store.keySet())); //store meta data cell

        cells.add(Cell.getFixedCellWithName(Cell.BUSY));
        cells.add(Cell.getFixedCellWithName(Cell.NEXT_LOC));
        cells.add(new ObjectStoreCell());
        return cells;
    }

    private Collection<KCondition> extractAllPostCond(ArrayList<Expression> postCondList,
                                                      ArrayList<SingleVariableDeclaration> formalParams) {
        Collection<KCondition> allPostCond = new ArrayList<>();

        postCondList.forEach(postCondExpr ->
                allPostCond.add(KCondition.genKConditionFromJavaExpr(postCondExpr, formalParams)));

        //also include the constraint related to the return expression.
        //TODO
        return allPostCond;
    }

    private Collection<KCondition> extractAllPreCond(ArrayList<Expression> preCondList,
                                                     ArrayList<SingleVariableDeclaration> formalParams) {
        Collection<KCondition> allPreCond = new ArrayList<>();
        //include the default param range conditions
        formalParams.stream().filter(varDecl -> varDecl.getType().isPrimitiveType())
                .forEach(primVar ->
                        allPreCond.add(
                                KCondition.genKConditionFromConstraintString(
                                        ConstraintGen.genRangeConstraint4Type
                                                (primVar.getType().toString(), TypeMapping.convert2KVar
                                                        (primVar.getName().toString(), true)))));

        preCondList.forEach(preCondExpr ->
                allPreCond.add(KCondition.genKConditionFromJavaExpr(preCondExpr, formalParams)));
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
}
