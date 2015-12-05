package transform.ast.rewrite;

import org.eclipse.jdt.core.dom.ITypeBinding;
import transform.ast.KCondition;
import transform.utils.ConstraintGen;
import transform.utils.TypeMapping;
import transform.utils.Utils;

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

    public KCondition genConstraint() {
        return KCondition.genKConditionFromConstraintString
                (ConstraintGen.genRangeConstraint4Type(this.javaTypeStr, this.rhs));
    }

    public boolean isPrimitiveDataType() {
        return this.isPrimitiveType;
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
