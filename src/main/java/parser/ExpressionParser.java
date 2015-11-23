package parser;

import org.eclipse.jdt.core.dom.*;
import transform.ast.CondExpression;
import transform.utils.TypeMapping;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xiaohe on 10/9/15.
 */
public class ExpressionParser extends ASTVisitor {
    private HashMap<String, String> typeEnv = new HashMap<>();

    private static final ASTParser expParser = initParser();

    private static int typeIdOfTheOperands = TypeMapping.OTHER_OPERAND;

    private static void resetTypeId() {
        typeIdOfTheOperands = TypeMapping.OTHER_OPERAND;
    }

    public ExpressionParser(ArrayList<SingleVariableDeclaration> formalParams) {
        formalParams.forEach(varDecl -> typeEnv.put(varDecl.getName().toString(), varDecl
                .getType().toString()));
    }

    public ExpressionParser(HashMap<String, String> typeEnv0) {
        this.typeEnv.putAll(typeEnv0);
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
     *
     * @param exprStr
     * @return
     */
    public static int getTypeIdOfTheExpr(String exprStr,
                                         ArrayList<SingleVariableDeclaration> formalParams) {
        Expression expr = parseExprStr(exprStr);

        resetTypeId();

        expr.accept(new ExpressionParser(formalParams));

        return typeIdOfTheOperands;
    }

    /**
     * Return the type id (see the def in TypeMapping class) of the operands of the expression.
     *
     * @param exprStr
     * @return
     */
    public static int getTypeIdOfTheExpr(String exprStr,
                                         HashMap<String, String> typeEnv0) {
        Expression expr = parseExprStr(exprStr);

        resetTypeId();

        expr.accept(new ExpressionParser(typeEnv0));

        return typeIdOfTheOperands;
    }

    public boolean visit(SimpleName name) {
        String type = this.typeEnv.get(name.toString());

//        System.out.println("Visit simple name " + name);

        typeIdOfTheOperands = TypeMapping.getTypeId(type);
        return false;
    }

    public boolean visit(NumberLiteral numberLiteral) {
//        System.out.println("Visit number literal " + numberLiteral);

        boolean isInt = false;
        try {
            long num = Long.parseLong(numberLiteral + "");
            isInt = true;
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }

        if (isInt)
            typeIdOfTheOperands = TypeMapping.INT_OPERAND;
        else
            typeIdOfTheOperands = TypeMapping.FLOAT_OPERAND;

        return false;
    }

    public boolean visit(InfixExpression exp) {
//        System.out.println("Visit exp " + exp);
//        System.out.println("The operator is " + exp.getOperator());
//        System.out.println("Left operand is " + exp.getLeftOperand());
//        System.out.println("Right operand is " + exp.getRightOperand());
        return true;
    }


    public static void main(String[] args) {
        HashMap<String, String> myTyEnv = new HashMap<>();
        myTyEnv.put("a", "int");
        myTyEnv.put("bb", "int");
        myTyEnv.put("c", "float");
        myTyEnv.put("ccc", "int");
        myTyEnv.put("d", "String");

        ExpressionParser myExpVisitor = new ExpressionParser(myTyEnv);
        String test1 = "a >         (bb + ccc)* d - 2";

        Expression exp1 = parseExprStr(test1);
        System.out.println(exp1);

        exp1.accept(myExpVisitor);

        String test2 = "a > bb + c * d - 2.2";
        Expression exp2 = parseExprStr(test2);
        System.out.println(exp2);

        exp2.accept(myExpVisitor);

        System.out.println("The corresponding k op for exp1 is: ");
        System.out.println(CondExpression.transformJExpr2KExpr(exp1));

        System.out.println("The corresponding k op for exp2 is: ");
        System.out.println(CondExpression.transformJExpr2KExpr(exp2));

        System.out.println("The type of exp1 is " + getTypeIdOfTheExpr(test1, myTyEnv));
        System.out.println("The type of exp2 is " + getTypeIdOfTheExpr(test2, myTyEnv));

        Expression exp3 = parseExprStr("1 < 2 <= 3");
        System.out.println(exp3 + " is also ok.");
    }
}
