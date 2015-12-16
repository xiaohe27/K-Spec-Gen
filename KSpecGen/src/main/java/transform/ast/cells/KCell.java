package transform.ast.cells;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import parser.ExpressionParser;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import parser.annotation.Patterns;
import transform.utils.CellContentGenerator;
import transform.utils.TypeMapping;
import transform.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by hx312 on 31/10/2015.
 */
public class KCell extends Cell {
    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    private List<String> paramRefNames = new ArrayList<>();

    private final ArrayList<SingleVariableDeclaration> methArgs = new ArrayList<>();


    public KCell(MethodInfo methodInfo, LoopInfo loopInfo, List<String> objStore) {
        super(Cell.K);
        this.hasRightOmission = true;
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;
        this.constructParamRefNames(objStore);
        this.methArgs.addAll(this.methodInfo.getFormalParams());
    }

    private void constructParamRefNames(List<String> objStore) {
        if (objStore == null || objStore.isEmpty())
            return;

        for (int i = 0; i < objStore.size(); i++) {
            String entryI = objStore.get(i);
            Matcher matcherI = Patterns.objStoreEntry.matcher(entryI);
            if (matcherI.find()) {
                this.paramRefNames.add(matcherI.group(1));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.loopInfo == null) {
            sb.append(this.methodInfo.className2ID() + ".");
            sb.append(this.methodInfo.methodName2ID());
            sb.append(":Id(");

            // if the current pos of paramRefNames is NOT empty,
            // then we have suggested k var name for the method param.
            // we use paramIndex to count the current index for obj type param
            int paramIndex = 0;
            for (int i = 0; i < this.methArgs.size() - 1; i++) {
                SingleVariableDeclaration curVar = this.methArgs.get(i);
                if (paramIndex < this.paramRefNames.size() && !curVar.getType().isPrimitiveType()) {
                    sb.append(TypeMapping.convert2KType(curVar, this.paramRefNames.get(paramIndex++)));
                } else {
                    sb.append(TypeMapping.convert2KType(curVar) + ", ");
                }
            }

            int lastIndex = this.methArgs.size() - 1;
            SingleVariableDeclaration lastVar = this.methArgs.get(lastIndex);
            if (paramIndex < this.paramRefNames.size() && !lastVar.getType().isPrimitiveType()) {
                sb.append(TypeMapping.convert2KType(lastVar, this.paramRefNames.get(paramIndex++)));
            } else {
                sb.append(TypeMapping.convert2KType(lastVar));
            }

            sb.append(")" + Utils.NEW_LINE);

            sb.append("=>" + Utils.NEW_LINE);

            Type retType = this.methodInfo.getRetType();
            String retTypeInJavaSemantics = retType.isPrimitiveType() ? retType.toString() :
                    Utils.className2ID(retType.toString());

            if (retTypeInJavaSemantics.equals("boolean"))
                retTypeInJavaSemantics = "bool";

            String retKType = TypeMapping.getKBuiltInType4SimpleJType(retType.toString());
            String retVal = this.methodInfo.getExpectedRetVal();
            if (retVal.startsWith("?")) {
                retVal = retVal.toUpperCase();
            } else {
                //transform all the program vars to K vars.
                Expression retExpr = ExpressionParser.parseExprStr(retVal);
                retVal = "(" + CellContentGenerator.fromJExpr2KExprString(retExpr,
                        this.methArgs).trim() + ")";
            }
            sb.append(retVal + ":" + retKType + "::" + retTypeInJavaSemantics);
        } else {
            sb.append(this.loopInfo.getLoopASTString());
            sb.append("=> .K ");
        }
        return super.surroundWithTags(sb.toString());
    }

}
