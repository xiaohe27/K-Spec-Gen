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
}
