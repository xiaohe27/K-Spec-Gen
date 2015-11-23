package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.ExpressionParser;
import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 19/11/2015.
 */
public class TypeMapping {
    /**
     * The type of the operands, 0 for int and 1 for floating point numbers.
     */
    public static final int INT_OPERAND = 0;
    public static final int FLOAT_OPERAND = 1;
    public static final int STRING_OPERAND = 2;
    public static final int OTHER_OPERAND = 3;

    private final static String[] intTypes = {"byte", "short", "char", "int", "long"};
    private final static String[] floatTypes = {"float", "double"};
    private final static String[] prefixOp = {"!", "-", "+"};

    private final static String[] commonInfixOP = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=",
            "==", "!="};

    private final static String[] intSpecOP = {"<<", ">>", "~", "^", "&", "|"};
    private final static String[] boolOP = {"&&", "||"};

    private final static String[] infixOP = initInfixOPs();

    private static String[] initInfixOPs() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < commonInfixOP.length; i++) {
            list.add(commonInfixOP[i]);
        }
        for (int i = 0; i < intSpecOP.length; i++) {
            list.add(intSpecOP[i]);
        }

        String[] tmpStrArr = new String[commonInfixOP.length + intSpecOP.length];
        return list.toArray(tmpStrArr);
    }


    private static boolean isIn(String[] strArr, String tarStr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(tarStr))
                return true;
        }
        return false;
    }

    public static int getTypeId(String type) {
        if (isIn(intTypes, type)) {
            return INT_OPERAND;
        } else if (isIn(floatTypes, type)) {
            return FLOAT_OPERAND;
        } else if ("String".equals(type)) {
            return STRING_OPERAND;
        } else {
            return OTHER_OPERAND;
        }
    }

    private static String getKBuiltInType4SimpleJType(String javaType) {
        if (isIn(intTypes, javaType))
            return "Int";

        if (isIn(floatTypes, javaType))
            return "Float";

        if ("String".equals(javaType))
            return "String";

        if ("boolean".equals(javaType))
            return "Bool";

        //it is not a basic type, then it should be a ref type.
        return "RawRefVal";
    }

    /**
     * Generate a fresh var based on 'baseName'.
     * The general strategy is
     * 1) capitalize the baseName to generate word w.
     * 2) if it is a primitive type, then goto 3), otherwise goto 4).
     * 3) If the string w produced in step 1) is different from the baseName, then return w; if
     * they are the same, then return w + "_raw".
     * 4) return w + "P".
     *
     * @param baseName
     * @param isPrimitive
     * @return
     */
    public static String freshVar(String baseName, boolean isPrimitive) {
        if (baseName == null)
            return "NULL";

        //step 1
        String freshVar = baseName.toUpperCase();

        //step 2
        if (isPrimitive) {
            //step 3
            if (freshVar.equals(baseName)) {
                return freshVar + "_raw";
            } else {
                return freshVar;
            }
        } else {
            //step 4
            return freshVar + "P";
        }
    }

    public static String convert2KType(SingleVariableDeclaration v) {
        String jType = v.getType().toString();
        String varName = v.getName().toString();
        boolean isPrimitive = v.getType().isPrimitiveType();

        String jTypeInJavaSemantics = isPrimitive ? jType : MethodInfo.className2ID(jType);

        String result = freshVar(varName, isPrimitive);
        result += ":" + getKBuiltInType4SimpleJType(jType);
        result = isPrimitive ? result : "(" + result + ")";
        result += "::" + jTypeInJavaSemantics;
        return result;
    }

    public static String convert2KOP(String oldOp, int operandType) {
        if ("!".equals(oldOp)) {
            return "notBool";
        } else if ("&&".equals(oldOp)) {
            return "andBool";
        } else if ("||".equals(oldOp)) {
            return "orBool";
        }

        switch (operandType) {
            case INT_OPERAND:
                return covert2KOp_Int(oldOp);

            case FLOAT_OPERAND:
                return covert2KOp_Float(oldOp);

            case STRING_OPERAND:
                return covert2KOp_String(oldOp);

            default:
                return oldOp;

        }
    }

    public static String covert2KOp_Int(String oldOp) {
        if ("!=".equals(oldOp)) {
            return "=/=Int";
        } else if (isIn(infixOP, oldOp)) {
            return oldOp + "Int";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_Float(String oldOp) {
        if ("!=".equals(oldOp)) {
            return "=/=Float";
        } else if (isIn(commonInfixOP, oldOp)) {
            return oldOp + "Float";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_String(String oldOp) {
        String[] excludeList = {"-", "*", "/", "%"};
        if ("!=".equals(oldOp)) {
            return "=/=String";
        } else if (isIn(commonInfixOP, oldOp) && !isIn(excludeList, oldOp)) {
            return oldOp + "String";
        } else {
            return oldOp;
        }
    }

    /**
     * Construct the KExpr from JavaExpression; Decompose the jexpr into conjunctions of
     * disjunctions, and transform each disjunction separately. Then the final result can be a
     * combination of sub-results via 'andBool' K-operator.
     *
     * @param jexpr
     * @param formalParams
     * @return
     */
    public static String fromJExpr2KExpr(String jexpr, ArrayList<SingleVariableDeclaration>
            formalParams) {
        String[] disjuncts = jexpr.split("&&");


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < disjuncts.length - 1; i++) {
            String disjunction = disjuncts[i];
            String subResult = fromDisjunct2KExpr(disjunction, formalParams);
            sb.append(subResult + " andBool ");
        }

        sb.append(fromDisjunct2KExpr(disjuncts[disjuncts.length - 1], formalParams) + "\n");

        return sb.toString();
    }

    private static String fromDisjunct2KExpr(String disjunction, ArrayList<SingleVariableDeclaration> formalParams) {
        String[] literals = disjunction.split("||");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.length; i++) {
            String literal = literals[i];
            int typeId = ExpressionParser.getTypeIdOfTheExpr(literal, formalParams);
            String subResult = fromLiteral2KExpr(literal, typeId);
            sb.append(subResult + (i == literals.length - 1 ? " " : " orBool "));
        }

        return sb.toString();
    }

    /**
     * We can assume that there is neither '&&' nor '||' in the literal, and focus on
     * transforming the literal according to the
     *
     * @param literal
     * @param typeId
     * @return
     */
    private static String fromLiteral2KExpr(String literal, int typeId) {
        return null;
    }

    public static void main(String[] args) {
        String int_v = "v";
        String bst_t = "t";
        System.out.println(freshVar(int_v, true));
        System.out.println(freshVar(bst_t, false));

        String test = "2 + 3 / 4 == 5 && 1.1 - 0.9 == -0.2";
        System.out.println(fromJExpr2KExpr(test));
    }
}
