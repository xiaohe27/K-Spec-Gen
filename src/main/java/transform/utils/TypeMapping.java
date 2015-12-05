package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.ExpressionParser;

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
    public static final int BOOL_OPERAND = 3;
    public static final int OTHER_OPERAND = 4;

    public final static String[] intTypes = {"byte", "short", "char", "int", "long"};
    public final static String[] floatTypes = {"float", "double"};
    public final static String[] prefixOp = {"!", "-", "+"};

    public final static String[] commonInfixOP = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=",
            "==", "!="};

    public final static String[] intSpecOP = {"<<", ">>", "~", "^", "&", "|"};
    public final static String[] boolOP = {"&&", "||", "!=", "==", "!"};

    public final static String[] infixOP = initInfixOPs();

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


    public static boolean isIn(String[] strArr, String tarStr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(tarStr))
                return true;
        }
        return false;
    }

    public static int getTypeId(String type) {
        if (type == null)
            return OTHER_OPERAND;

        if (isIn(intTypes, type)) {
            return INT_OPERAND;
        } else if (isIn(floatTypes, type)) {
            return FLOAT_OPERAND;
        } else if ("boolean".equals(type)) {
            return BOOL_OPERAND;
        } else if ("String".equals(type)) {
            return STRING_OPERAND;
        } else {
            return OTHER_OPERAND;
        }
    }

    public static String getKBuiltInType4SimpleJType(String javaType) {
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
     * Generate the k var name based on 'baseName'.
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
    public static String convert2KVar(String baseName, boolean isPrimitive) {
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
            if (!freshVar.equals(baseName) && freshVar.endsWith("P"))
                return freshVar;

            return freshVar + "P";
        }
    }

    public static String convert2KType(SingleVariableDeclaration v) {
        String jType = v.getType().toString();
        String varName = v.getName().toString();
        boolean isPrimitive = v.getType().isPrimitiveType();

        String jTypeInJavaSemantics = isPrimitive ? jType : Utils.className2ID(jType);
        if (jTypeInJavaSemantics.equals("boolean"))
            jTypeInJavaSemantics = "bool";

        String result = convert2KVar(varName, isPrimitive);
        result += ":" + getKBuiltInType4SimpleJType(jType);
        result = isPrimitive ? result : "(" + result + ")";
        result += "::" + jTypeInJavaSemantics;
        return result;
    }

    public static String convert2KOP(String oldOp, int operandType) {
        oldOp = oldOp.trim();
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

            case BOOL_OPERAND:
                return convert2KOp_Bool(oldOp);

            default:
                return oldOp;

        }
    }

    private static String convert2KOp_Bool(String oldOp) {
        if ("==".equals(oldOp)) {
            return "==Bool";
        } else if ("!=".equals(oldOp)) {
            return "=/=Bool";
        } else {
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

    public static void main(String[] args) {
        String goodExp = ExpressionParser.parseExprStr("min(a,b) + c -d/e >= 72").toString();
        System.out.println(CellContentGenerator.fromLiteral2KExpr(goodExp, TypeMapping.INT_OPERAND));
    }

}
