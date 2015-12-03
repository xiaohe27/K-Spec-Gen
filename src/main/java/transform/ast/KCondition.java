package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import transform.utils.TypeMapping;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KCondition extends KASTNode {
    private Expression expression = null;
    private ArrayList<SingleVariableDeclaration> params = null;
    private Set<SimpleName> names = null;

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

    private KCondition(Expression expr, Set<SimpleName> vars) {
        super("'kcondition");
        this.expression = expr;
        this.names = vars;
    }

    public static KCondition genKConditionFromConstraintString(String condExpr) {
        return new KCondition(condExpr);
    }

    public static KCondition genKConditionFromJavaExpr(Expression expr,
                                                       ArrayList<SingleVariableDeclaration> params) {
        return new KCondition(expr, params);
    }

    public static KCondition genKConditionFromJavaExpr(Expression expr,
                                                       Set<SimpleName> vars) {
        return new KCondition(expr, vars);
    }

    public String toString() {
        if (this.condString == null) {
            if (this.names == null)
                this.condString = TypeMapping.fromJExpr2KExprString(this.expression, this.params);
            else
                this.condString = TypeMapping.fromJExpr2KExprString(this.expression, this.names);
        }

        return this.condString;
    }

}
