package transform.utils;

import org.eclipse.jdt.core.dom.Expression;

/**
 * Created by hx312 on 29/11/2015.
 */
public class KAST_Transformer {
    /**
     * Convert a java expression ast to k expression ast.
     *
     * @param jexp
     * @return
     */
    public static String convert2KAST(Expression jexp) {
        switch (jexp.getNodeType()) {
            case Expression.INFIX_EXPRESSION:
                break;

            case Expression.PREFIX_EXPRESSION:
                break;

            //The var name expr
            case Expression.SIMPLE_NAME:
                break;

            //The constants
            case Expression.NUMBER_LITERAL:

            case Expression.STRING_LITERAL:

            case Expression.BOOLEAN_LITERAL:
                break;
        }

        return "";
    }
}
