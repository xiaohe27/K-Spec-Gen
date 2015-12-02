package transform.utils;

import org.eclipse.jdt.core.dom.*;

/**
 * Created by hx312 on 29/11/2015.
 */
public class KAST_Transformer {
    public static String[] boolOPs = {"<", "<=", ">", ">=", "==", "!=", "&&", "||", "!"};

    private static boolean isBoolOp(String op) {
        return TypeMapping.isIn(boolOPs, op);
    }

    public static boolean areCompatibleTypes(String type1, String type2) {
        return TypeMapping.getTypeId(type1) == TypeMapping.getTypeId(type2);
    }

    private static String _KExpr(String id) {
        return "'ExprName(" + id + ")";
    }

    public static String cast2Type(String expr, String type) {
        return "cast ( " + type + ", " + expr + ")";
    }

    private static String convertSimpleName2KASTString(SimpleName var, boolean needCast) {
        String id = var.getIdentifier();
        ITypeBinding iTypeBinding = var.resolveTypeBinding();
        String type = iTypeBinding == null ? "Object" : iTypeBinding.toString();
        String kExpr = _KExpr(Utils.string2ID(var.getIdentifier()));
        return needCast ? cast2Type(kExpr, type) : kExpr;
    }

    /**
     * Convert a java expression ast to k expression ast.
     *
     * @param jexp
     * @return
     */
    public static String convert2KAST(Expression jexp, boolean needCast) throws Exception {
        switch (jexp.getNodeType()) {
            case Expression.INFIX_EXPRESSION:
                InfixExpression infixExp = (InfixExpression) jexp;
                String lhsTy = infixExp.getLeftOperand().resolveTypeBinding().getName();
                String rhsTy = infixExp.getRightOperand().resolveTypeBinding().getName();

                if (!areCompatibleTypes(lhsTy, rhsTy))
                    throw new Exception("Type " + lhsTy + " and " + rhsTy + " cannot be operated " +
                            "by the operator " + infixExp.getOperator() + " in K");

                String infixOP = infixExp.getOperator().toString();

                boolean lhsCast = needCast;
                if (infixOP.equals("=")) {
                    lhsCast = false;
                }

                String exprStr = convert2KAST(infixExp.getLeftOperand(), lhsCast) + infixOP
                        + convert2KAST(infixExp.getRightOperand(), needCast);

                String combinedType = "";
                if (isBoolOp(infixOP)) {
                    combinedType = "bool";
                } else {
                    combinedType = lhsTy;
                }

                if (infixOP.equals("=")) {
                    exprStr = "(" + exprStr + ")::AssignExp";
                }

                return cast2Type(exprStr, combinedType);

            case Expression.PREFIX_EXPRESSION:
                PrefixExpression prefixExpression = (PrefixExpression) jexp;
                String type = prefixExpression.getOperator().toString();
                if (type.equals("!")) {
                    String operand = convert2KAST(prefixExpression.getOperand(), needCast);
                    return cast2Type("! " + operand, "bool");
                }
                break;

            //The var name expr
            case Expression.SIMPLE_NAME:
                SimpleName simpleName = (SimpleName) jexp;
                return convertSimpleName2KASTString(simpleName, needCast);

            //The constants
            case Expression.NUMBER_LITERAL:

            case Expression.STRING_LITERAL:

            case Expression.BOOLEAN_LITERAL:

                String const_type = jexp.resolveTypeBinding().toString();
                if (const_type.equals("boolean"))
                    const_type = "bool";

                return jexp.toString() + "::" + const_type;

            case Expression.NULL_LITERAL:
                return jexp.toString() + " :: nullType";
        }

        return jexp.toString();
    }
}
