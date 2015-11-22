package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.MethodInfo;

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
    private final static String[] infixOp = {"+", "-", "*", "/", "^", "%", "<", "<=", ">", ">=",
            "==", "&&", "||",
            "<<", ">>"};

    private static boolean isIn(String[] strArr, String tarStr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(tarStr))
                return true;
        }
        return false;
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
        switch (operandType) {
            case INT_OPERAND :
                return covert2KOp_Int(oldOp);

            case FLOAT_OPERAND :
                return covert2KOp_Float(oldOp);

            case STRING_OPERAND :
                return covert2KOp_String(oldOp);

            default: return oldOp;

        }
    }

    public static String covert2KOp_Int(String oldOp) {
        if (isIn(infixOp, oldOp)) {
            return oldOp + "Int";
        } else if ("!".equals(oldOp)) {
            return "notBool";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_Float(String oldOp) {
        if (isIn(infixOp, oldOp)) {
            return oldOp + "Float";
        } else if ("!".equals(oldOp)) {
            return "notBool";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_String(String oldOp) {
        if (isIn(infixOp, oldOp)) {
            return oldOp + "String";
        } else if ("!".equals(oldOp)) {
            return "notBool";
        } else {
            return oldOp;
        }
    }

    public static void main(String[] args) {
        String int_v = "v";
        String bst_t = "t";
        System.out.println(freshVar(int_v, true));
        System.out.println(freshVar(bst_t, false));
    }
}
