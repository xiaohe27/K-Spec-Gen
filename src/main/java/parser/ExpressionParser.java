package parser;

import org.eclipse.jdt.core.dom.*;

/**
 * Created by xiaohe on 10/9/15.
 */
public class ExpressionParser extends ASTVisitor {
    private static final ASTParser expParser = initParser();

    private static ASTParser initParser() {
        ASTParser expParser = ASTParser.newParser(AST.JLS8);
        return expParser;
    }

    public static Expression parseExprStr(String str) {
        expParser.setSource(str.toCharArray());
        expParser.setKind(ASTParser.K_EXPRESSION);

        return ((Expression) expParser.createAST(null));
    }

    public boolean visit(InfixExpression exp) {
        System.out.println("Visit exp " + exp);
        System.out.println("The operator is " + exp.getOperator());
        System.out.println("Left operand is " + exp.getLeftOperand());
        System.out.println("Right operand is " + exp.getRightOperand());

        return true;
    }

    public static void main(String[] args) {
        ExpressionParser myExpVisitor = new ExpressionParser();
        String test1 = "a >         (b + c)* d - 2";

        Expression exp1 = parseExprStr(test1);
        System.out.println(exp1);

        exp1.accept(myExpVisitor);

        String test2 = "a > b + c * d - 2";
        Expression exp2 = parseExprStr(test2);
        System.out.println(exp2);

        exp2.accept(myExpVisitor);
    }
}
