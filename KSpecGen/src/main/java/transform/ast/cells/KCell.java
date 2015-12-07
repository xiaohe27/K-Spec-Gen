package transform.ast.cells;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import parser.ExpressionParser;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import transform.utils.CellContentGenerator;
import transform.utils.TypeMapping;
import transform.utils.Utils;

import java.util.ArrayList;

/**
 * Created by hx312 on 31/10/2015.
 */
public class KCell extends Cell {
    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    private final ArrayList<SingleVariableDeclaration> methArgs = new ArrayList<>();


    public KCell(MethodInfo methodInfo, LoopInfo loopInfo) {
        super(Cell.K);
        this.hasRightOmission = true;
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;

        this.methArgs.addAll(this.methodInfo.getFormalParams());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.loopInfo == null) {
            sb.append(this.methodInfo.className2ID() + ".");
            sb.append(this.methodInfo.methodName2ID());
            sb.append(":Id(");

            for (int i = 0; i < this.methArgs.size() - 1; i++) {
                sb.append(TypeMapping.convert2KType(this.methArgs.get(i)) + ", ");
            }

            sb.append(TypeMapping.convert2KType(this.methArgs.get(this.methArgs.size() - 1)));
            sb.append(")\n");

            sb.append("=>\n");

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
                retVal = "(" + CellContentGenerator.fromJExpr2KExprString(retExpr, this.methArgs).trim() + ")";
            }
            sb.append(retVal + ":" + retKType + "::" + retTypeInJavaSemantics);
        } else {
            sb.append(this.loopInfo.getLoopASTString());
            sb.append("=> .K ");
        }
        return super.surroundWithTags(sb.toString());
    }

}
