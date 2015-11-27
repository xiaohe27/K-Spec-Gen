package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import transform.utils.TypeMapping;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KCondition extends KASTNode {
    private Expression expression = null;
    private ArrayList<SingleVariableDeclaration> params = new ArrayList<>();

    private String condString = null;

    private KCondition(String condExpr) {
        super("'kcondition");
        this.condString = condExpr;
    }

    private KCondition(Expression expr, ArrayList<SingleVariableDeclaration> params) {
        super("'kcondition");
        this.expression = expr;
        this.params = params;
    }

    public static KCondition genKConditionFromConstraintString(String condExpr) {
        return new KCondition(condExpr);
    }

    public static KCondition genKConditionFromJavaExpr(Expression expr,
                                                       ArrayList<SingleVariableDeclaration> params) {
        return new KCondition(expr, params);
    }

    public String toString() {
        if (this.condString == null) {
            this.condString = TypeMapping.fromJExpr2KExprString(this.expression, this.params);
        }

        return this.condString;
    }

}
