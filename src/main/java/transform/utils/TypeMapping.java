package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.MethodInfo;

/**
 * Created by hx312 on 19/11/2015.
 */
public class TypeMapping {
    private final static String[] intTypes = {"byte", "short", "char", "int", "long"};
    private final static String[] floatTypes = {"float", "double"};

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

    public static String getTypeInJavaSemantics(String type) {
        if (isIn(intTypes, type)) {
            return "int";
        }

        if ()
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

    public static void main(String[] args) {
        String int_v = "v";
        String bst_t = "t";
        System.out.println(freshVar(int_v, true));
        System.out.println(freshVar(bst_t, false));
    }
}
