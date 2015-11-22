package transform.ast;

import org.eclipse.jdt.core.dom.*;
import transform.utils.TypeMapping;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class CondExpression {
    private String op;
    private boolean isFunctionApp = false;
    private ArrayList<String> operands;

    /**
     * The type of the operands, 0 for int and 1 for floating point numbers.
     */
    public static final int INT_OPERAND = 0;
    public static final int FLOAT_OPERAND = 1;

    public CondExpression(String op, ArrayList<String> operands) {
        this(op, false, operands, INT_OPERAND);
    }

    public CondExpression(String op, boolean isFunctionApp, ArrayList<String> operands,
                          int operandType) {
        this.op = (operandType == INT_OPERAND ? TypeMapping.covert2KOp_Int(op) : (operandType ==
                FLOAT_OPERAND ? TypeMapping.covert2KOp_Float(op) : op));

        this.isFunctionApp = isFunctionApp;
        this.operands = operands;
    }

    public String toString() {
        if ("()".equals(op)) {
            //it is an () expr
            return "(" + operands.get(0) + ")";
        }

        if (operands == null || operands.size() <= 0) {
            return op;
        } else if (!this.isFunctionApp) { //the normal primitive operations.
            if (operands.size() == 1) {
                //it is uary op
                return op + "(" + operands.get(0) + ")";
            } else if (operands.size() == 2) {
                //it is bin op
                return "(" + operands.get(0) + ")" + op + "(" + operands.get(1) + ")";
            } else {
                return "No primitive op of size > 2 is supported at present";
            }

        } else {
            String ret = op + "(";
            for (int i = 0; i < operands.size() - 1; i++) {
                ret += operands.get(i) + ", ";
            }
            ret += operands.get(operands.size() - 1);
            ret += ")";
            return ret;
        }
    }

    public static CondExpression transformJExpr2KExpr(Expression operation) {
        System.out.println("The type of the exp is " + operation.getNodeType());
        ArrayList<String> operands = new ArrayList<>();

        if (operation instanceof InfixExpression) {
            InfixExpression infixExp = ((InfixExpression) operation);
            String op = infixExp.getOperator().toString();
            operands.add(transformJExpr2KExpr(infixExp.getLeftOperand()).toString());
            operands.add(transformJExpr2KExpr(infixExp.getRightOperand()).toString());
            return new CondExpression(op, operands);
        } else if (operation instanceof PrefixExpression) {
            PrefixExpression prefixExp = ((PrefixExpression) operation);
            String op = prefixExp.getOperator().toString();
            operands.add(transformJExpr2KExpr(prefixExp.getOperand()).toString());
            return new CondExpression(op, operands);
        } else if (operation instanceof MethodInvocation) {
            MethodInvocation methInvoc = (MethodInvocation) operation;
            String funcName = methInvoc.getName().getFullyQualifiedName();
            methInvoc.arguments().forEach(argExp ->
                    operands.add(transformJExpr2KExpr((Expression) argExp).toString()));
            return new CondExpression(funcName, operands);
        } else if (operation instanceof ParenthesizedExpression){
            ParenthesizedExpression parenExp = (ParenthesizedExpression) operation;
            String parenOp = "()";
            operands.add(transformJExpr2KExpr(parenExp.getExpression()).toString());
            return new CondExpression(parenOp, operands);
        }

        //leaves noes
        else if (operation instanceof StringLiteral || operation instanceof Name ||
                operation instanceof BooleanLiteral || operation instanceof NumberLiteral ||
                operation instanceof CharacterLiteral || operation instanceof NullLiteral) {
            String op = operation.toString();
            return new CondExpression(op, new ArrayList<>());
        } else {
            throw new UnsupportedOperationException("The expression " + operation + " is not supported at " +
                    "present.");
        }
    }
}
