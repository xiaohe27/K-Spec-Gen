package transform.utils;

import org.eclipse.jdt.core.dom.SimpleName;

import java.util.HashMap;

/**
 * Created by hx312 on 29/11/2015.
 */
public class Utils {
    public static boolean contains(HashMap<SimpleName, Integer> map, SimpleName var) {
        for (SimpleName curVar: map.keySet()) {
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

    /**
     * Transform the method name to K's ID.
     *
     * @return
     */
    public static String methodName2ID(String methName) {
        return string2ID(methName);
    }
}
