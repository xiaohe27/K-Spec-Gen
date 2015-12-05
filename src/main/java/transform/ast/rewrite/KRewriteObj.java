package transform.ast.rewrite;

import javafx.util.Pair;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import parser.ExpressionParser;
import transform.ast.KCondition;
import transform.utils.ConstraintGen;
import transform.utils.TypeMapping;
import transform.utils.Utils;

import java.util.Set;

/**
 * Created by hx312 on 12/1/2015.
 * A K rewrite obj represents a rewrite step of the form:
 * lhs => rhs
 * where lhs and rhs are k-expr.
 * N.B. when rhs is null, it represents a special form of value where no rewrite occurs.
 */
public class KRewriteObj {
    private final String javaTypeStr;
    private final String lhs;
    private final String rhs;

    private final boolean isRHSFresh;

    private final String kBuiltInType;
    private String javaTypeInK;
    private boolean isPrimitiveType;


    public KRewriteObj(ITypeBinding javaType, String lhs, String rhs) {
        this(javaType, lhs, rhs, false);
    }

    public KRewriteObj(ITypeBinding javaType, String lhs, String rhs, boolean isRHSFreshVar) {
        this.javaTypeStr = javaType.getName();
        this.lhs = lhs;
        this.rhs = rhs;

        this.isRHSFresh = isRHSFreshVar;

        this.kBuiltInType = TypeMapping.getKBuiltInType4SimpleJType(this.javaTypeStr);
        this.isPrimitiveType = javaType.isPrimitive();

        this.javaTypeInK = this.isPrimitiveType ? this.javaTypeStr
                : Utils.className2ID(this.javaTypeStr, false);

        if (javaTypeInK.equals("boolean"))
            javaTypeInK = "bool";

    }

    public static final Pair<Boolean, String[]> getParamsOfRewriteObj(String valStr, Set<SimpleName>
            localVars) {
        valStr = Utils.removeBrace(valStr);

        final String[] elements = valStr.split("=>");
        boolean rhsIsFresh = elements.length == 2 && elements[1].trim().startsWith("?");

        for (int i = 0; i < elements.length; i++) {
            Expression expI = ExpressionParser.parseExprStr
                    (elements[i].replaceAll("\\?", ""));
            //transform to k expr where every op has been transformed
            elements[i] = TypeMapping.fromJExpr2KExprString(expI, localVars).trim();

            //N.B. the meta-variables in the expression may not be renamed in the
            // above process, so manually rename them if necessary.
            if (expI.toString().equals(elements[i]) && expI instanceof SimpleName) {
                elements[i] = TypeMapping.convert2KVar(elements[i], true);
            }
        }

        return new Pair<Boolean, String[]>(rhsIsFresh, elements);
    }

    public KCondition genConstraint() {
        return KCondition.genKConditionFromConstraintString
                (ConstraintGen.genRangeConstraint4Type(this.javaTypeStr, this.rhs));
    }

    public boolean isPrimitiveDataType() {
        return this.isPrimitiveType;
    }

    public String rewrite2KVarIfPossible(String str, boolean isLHS) {
        if (isLHS) {
            return str.replaceAll(this.lhs.toLowerCase(), this.lhs);
        } else {
            return str.replaceAll(this.rhs.toLowerCase(), this.rhs);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs + ":" + this.kBuiltInType);
        if (this.rhs != null) {
            sb.append(" => ");
            if (this.isRHSFresh)
                sb.append("?");
            sb.append(rhs);
            if (this.isRHSFresh)
                sb.append(":RawRefVal");
        }
        return Utils.addBrackets(sb.toString()) + " :: " + this.javaTypeInK;
    }
}
