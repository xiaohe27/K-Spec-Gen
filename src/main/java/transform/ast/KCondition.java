package transform.ast;

import org.eclipse.jdt.core.dom.Expression;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KCondition extends KASTNode {

    private CondExpression expression;

    private String condString;

    public KCondition(String condExpr) {
        super("'kcondition");
        this.condString = condExpr;
    }

    public String toString() {
        return this.condString;
    }

}
