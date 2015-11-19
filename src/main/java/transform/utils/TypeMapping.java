package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.MethodInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public static String convert2KType(SingleVariableDeclaration v) {
        String jTypeInKSpec = v.getType().isPrimitiveType() ? v.getType().toString() : MethodInfo
                .className2ID(v.getType().toString());

        return
    }
}
