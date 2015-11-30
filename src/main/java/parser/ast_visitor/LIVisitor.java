package parser.ast_visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.InfixExpression;

/**
 * Created by hx312 on 30/11/2015.
 */
public class LIVisitor extends ASTVisitor {
    public boolean visit(InfixExpression infixExp) {
        //TODO
        System.out.println("LHS of this LI is " + infixExp.getLeftOperand());
        System.out.println("OP of this LI is " + infixExp.getOperator());
        System.out.println("RHS of this LI is " + infixExp.getRightOperand());

        return true;
    }

    public boolean visit(Assignment assignment) {
        //TODO
        System.out.println("LHS of this LI assignment is " + assignment.getLeftHandSide());
        System.out.println("RHS of this LI assignment is " + assignment.getRightHandSide());
        return true;
    }
}
