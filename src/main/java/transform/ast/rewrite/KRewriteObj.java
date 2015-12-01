package transform.ast.rewrite;

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
    private String javaType;
    private String lhs;
    private String rhs;

    private String kBuiltInType;
    private String javaTypeInK;

    public KRewriteObj(String javaType, String lhs, String rhs) {
        this.javaType = javaType;
        this.lhs = lhs;
        this.rhs = rhs;

        this.kBuiltInType = TypeMapping.getKBuiltInType4SimpleJType(this.javaType);
        this.javaTypeInK = Utils.className2ID(this.javaType, false);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs + ":" + this.kBuiltInType);
        if (this.rhs != null) {
            sb.append(" => ");
            sb.append(rhs);
        }
        return Utils.addBrackets(sb.toString()) + " :: " + this.javaTypeInK;
    }
}
