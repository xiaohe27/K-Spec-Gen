package transform.utils;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import java.util.HashMap;

/**
 * Created by hx312 on 29/11/2015.
 */
public class Utils {
    public static boolean contains(HashMap<SimpleName, Integer> map, SimpleName var) {
        for (SimpleName curVar : map.keySet()) {
            if (curVar.getIdentifier().equals(var.getIdentifier()))
                return true;
        }

        return false;
    }

    /**
     * Transform the given string to K's ID.
     */
    public static String string2ID(String inputStr) {
        return "String2Id(\"" + inputStr + "\")";
    }

    /**
     * Transform the class name to K's ID.
     *
     * @return
     */
    public static String className2ID(String clsName) {
        return "(class " + string2ID("." + clsName) + ")";
    }

    public static String addBrackets(String s) {
        return "(" + s + ")";
    }

    public static String className2ID(String clsName, boolean outermostBrace) {
        String ret = "class " + string2ID("." + clsName);
        return outermostBrace ? addBrackets(ret) : ret;
    }

    /**
     * Transform the method name to K's ID.
     *
     * @return
     */
    public static String methodName2ID(String methName) {
        return string2ID(methName);
    }

    public static boolean isInt(NumberLiteral numberLiteral) {
        boolean isInt = false;
        try {
            Long.parseLong(numberLiteral + "");
            isInt = true;
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        return isInt;
    }

    public static String convert2KAST_Type(Type jType) {
        String type = jType.toString();
        if (jType.isPrimitiveType()) {
            type = type.equals("boolean") ? "bool" : type;
        } else {
            type = "class ." + type;
        }
        return type;
    }

    public static String convert2KAST_Type(ITypeBinding jType) {
        String type = jType.getName();
        if (jType.isPrimitive()) {
            type = type.equals("boolean") ? "bool" : type;
        } else {
            type = "class ." + type;
        }
        return type;
    }
}
