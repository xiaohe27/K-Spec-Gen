package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import parser.ExpressionParser;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import transform.ast.cells.Cell;
import transform.ast.cells.ObjectStoreCell;
import transform.ast.cells.StoreCell;
import transform.ast.cells.ThreadsCell;
import transform.ast.rewrite.KRewriteObj;
import transform.utils.CellContentGenerator;
import transform.utils.ConstraintGen;
import transform.utils.TypeMapping;

import java.util.*;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KRule extends KASTNode {
    private boolean isLoop = false;
    private ArrayList<KCondition> preConds = new ArrayList<>();
    private ArrayList<KCondition> postConds = new ArrayList<>();
    private String retVal;
    private Type retType;
    private ArrayList<Cell> cells = new ArrayList<>();
    private HashMap<SimpleName, Integer> env = new HashMap<>(); //env maps vars to locations
    private HashMap<Integer, KRewriteObj> store = null; //store is shared between methods and loops.
    private List<String> objStore = new ArrayList<>();

    public KRule(MethodInfo methodInfo, HashMap<Integer, KRewriteObj> store0) {
        this(methodInfo, null, store0);
    }

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo, HashMap<Integer, KRewriteObj> store0) {
        super("'KRule");
        this.store = store0;
        this.retVal = methodInfo.getExpectedRetVal();
        this.retType = methodInfo.getRetType();

        if (loopInfo != null) {
            this.isLoop = true;
            loopInfo.updateEnvAndStore(this.env, this.store, this.objStore);
            rewriteLI(loopInfo);
        } else {
            CellContentGenerator.updateObjStoreByParsingContent(this.objStore,
                    methodInfo.getObjStoreContent(),
                    this.store.values(), methodInfo.getFormalParams());
        }

        this.preConds.addAll(extractAllPreCond(methodInfo.getPreCondList(), methodInfo.getFormalParams()));
        this.postConds.addAll(extractAllPostCond(methodInfo.getPostCondList(), methodInfo.getFormalParams()));
        this.cells = constructCells(methodInfo, loopInfo);
    }

    /**
     * Rewrite the LOOP_PROP java-expr to k-expr.
     *
     * @param loopInfo
     */
    private void rewriteLI(LoopInfo loopInfo) {
        final Set<SimpleName> vars = loopInfo.getSetOfVarNames();
        loopInfo.getLIStream().forEach(
                liExp -> {
                    this.preConds.add(KCondition.genKConditionFromJavaExpr(liExp, vars));
                }
        );

        loopInfo.getLoopPreCondStream().forEach(
                loopPre -> {
                    final String[] processedPreCond = new String[1];
                    processedPreCond[0] = CellContentGenerator.fromLiteral2KExpr
                            (loopPre, TypeMapping.OTHER_OPERAND);

                    vars.forEach(var -> {
                        processedPreCond[0] = processedPreCond[0]
                                .replaceAll(CellContentGenerator.name2Regex(var.getIdentifier()),
                                        var.getIdentifier().toUpperCase());
                    });

                    KCondition preCond = KCondition.genKConditionFromConstraintString
                            (processedPreCond[0]);
                    this.preConds.add(preCond);
                }
        );

        loopInfo.getLoopPostCondStream().forEach(
                loopPost -> {
                    final String[] processedPostCond = new String[1];
                    processedPostCond[0] = CellContentGenerator.fromLiteral2KExpr
                            (loopPost, TypeMapping.OTHER_OPERAND);

                    vars.forEach(var -> {
                        processedPostCond[0] = processedPostCond[0]
                                .replaceAll(CellContentGenerator.name2Regex(var.getIdentifier()),
                                        var.getIdentifier().toUpperCase());
                    });

                    KCondition postCond = KCondition.genKConditionFromConstraintString
                            (processedPostCond[0]);
                    this.postConds.add(postCond);
                }
        );
    }

    private ArrayList<Cell> constructCells(MethodInfo methodInfo, LoopInfo loopInfo) {
        ArrayList<Cell> cells = new ArrayList<>();
        ThreadsCell threadsCell = new ThreadsCell(methodInfo, loopInfo, this.env, this.objStore);
        cells.add(threadsCell);

        cells.add(Cell.getFixedCellWithName(Cell.CLASSES));
        cells.add(Cell.getFixedCellWithName(Cell.NumOfClassesToUnfold));
        cells.add(Cell.getFixedCellWithName(Cell.PROGRAM));
        cells.add(Cell.getFixedCellWithName(Cell.GlobalPhase));

        cells.add(new StoreCell(this.store).setDefinedLoc(this.env.values())); //store cell

        cells.add(Cell.getFixedCellWithName(Cell.BUSY));
        cells.add(Cell.getFixedCellWithName(Cell.NEXT_LOC));
        cells.add(new ObjectStoreCell(this.objStore));
        return cells;
    }

    private Collection<KCondition> extractAllPostCond(ArrayList<String> postCondList,
                                                      ArrayList<SingleVariableDeclaration> formalParams) {
        Collection<KCondition> allPostCond = new ArrayList<>();

        postCondList.forEach(postCondExprStr ->
        {
            String constraintStr = CellContentGenerator.fromJExpr2KExprString(
                    postCondExprStr, formalParams);
            allPostCond.add(KCondition.genKConditionFromConstraintString(constraintStr));
        });

        //also include the constraint related to the return expression.
        //the cell can be encoded in the ensures clause

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

        //the preconditions in the method contract
        preCondList.forEach(preCondExpr ->
                allPreCond.add(KCondition.genKConditionFromJavaExpr(preCondExpr, formalParams)));


        this.store.keySet().stream()
                .filter(loc -> (this.env.values().contains(loc)))
                .forEach(loc -> {
                    KRewriteObj kObj = this.store.get(loc);
                    if (kObj.isPrimitiveDataType())
                        allPreCond.add(kObj.genConstraint());
                });

        //include the range constraint of return expr.
        if (this.retType != null && this.retType.isPrimitiveType()
                && !this.isLoop && !this.retType.toString().equals("boolean")) {
            Expression retExpr = ExpressionParser.parseExprStr(this.retVal);
            String retKVal = CellContentGenerator.fromJExpr2KExprString(retExpr, formalParams).trim();

            allPreCond.add(KCondition.genKConditionFromConstraintString
                    (ConstraintGen.genRangeConstraint4Type(this.retType.toString(), retKVal)));
        }
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
