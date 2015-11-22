package parser;

import org.eclipse.jdt.core.dom.*;
import transform.ast.CondExpression;
import transform.utils.TypeMapping;

/**
 * Created by xiaohe on 10/9/15.
 */
public class ExpressionParser extends ASTVisitor {
    private static final ASTParser expParser = initParser();

    private static int typeIdOfTheOperands = TypeMapping.OTHER_OPERAND;

    private static void resetTypeId() {
        typeIdOfTheOperands = TypeMapping.OTHER_OPERAND;
    }

    private static ASTParser initParser() {
        ASTParser expParser = ASTParser.newParser(AST.JLS8);
        return expParser;
    }

    public static Expression parseExprStr(String str) {
        expParser.setSource(str.toCharArray());
        expParser.setKind(ASTParser.K_EXPRESSION);

        return ((Expression) expParser.createAST(null));
    }

    /**
     * Return the type id (see the def in TypeMapping class) of the operands of the expression.
     * @param exprStr
     * @return
     */
    public static int getTypeIdOfTheExpr(String exprStr) {
        Expression expr = parseExprStr(exprStr);

        resetTypeId();

        expr.accept(new ExpressionParser());

        return typeIdOfTheOperands;
    }

    public boolean visit(SimpleName name) {
        System.out.println("visit simple name " + name);

        return true;
    }

    public boolean visit(NumberLiteral numberLiteral) {
        boolean isInt = false;
        try {
            long num = Long.parseLong(numberLiteral+"");
            isInt = true;
        } catch(NumberFormatException e) {
        } catch(NullPointerException e) {
        }

        if (isInt)
            typeIdOfTheOperands = TypeMapping.INT_OPERAND;
        else
            typeIdOfTheOperands = TypeMapping.FLOAT_OPERAND;

        return true;
    }

    public boolean visit(InfixExpression exp) {
//        System.out.println("Visit exp " + exp);
//        System.out.println("The operator is " + exp.getOperator());
//        System.out.println("Left operand is " + exp.getLeftOperand());
//        System.out.println("Right operand is " + exp.getRightOperand());

        return true;
    }


    public static void main(String[] args) {
        ExpressionParser myExpVisitor = new ExpressionParser();
        String test1 = "a >         (bb + ccc)* d - 2";

        Expression exp1 = parseExprStr(test1);
        System.out.println(exp1);

        exp1.accept(myExpVisitor);

        String test2 = "a > b + c * d - 2.2";
        Expression exp2 = parseExprStr(test2);
        System.out.println(exp2);

        exp2.accept(myExpVisitor);

        System.out.println("The corresponding k op for exp1 is: ");
        System.out.println(CondExpression.transformJExpr2KExpr(exp1));

        System.out.println("The corresponding k op for exp2 is: ");
        System.out.println(CondExpression.transformJExpr2KExpr(exp2));

    }
}
