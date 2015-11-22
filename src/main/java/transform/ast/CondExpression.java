package transform.ast;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class CondExpression {
    private String op;
    private boolean isFunctionApp = false;
    private ArrayList<String> operands;

    public CondExpression(String op, ArrayList<String> operands) {
        this(op, false, operands);
    }

    public CondExpression(String op, boolean isFunctionApp, ArrayList<String> operands) {
        this.op = op;
        this.isFunctionApp = isFunctionApp;
        this.operands = operands;
    }

    public String toString() {
        if (operands == null || operands.size() <= 0) {
            return "";
        } else if (!this.isFunctionApp) { //the normal primitive operations.
            if (operands.size() == 1) {
                //it is uary op
                return op + "(" + operands.get(0) + ")";
            } else if (operands.size() == 2) {
                //it is bin op
                return "(" + operands.get(0) + ")" + op + "(" + operands.get(1) + ")";
            } else {
                return "No primitive op of size > 2 is supported at present";
            }

        } else {
            String ret = op + "(";
            for (int i = 0; i < operands.size() - 1; i++) {
                ret += operands.get(i) + ", ";
            }
            ret += operands.get(operands.size() - 1);
            ret += ")";
            return ret;
        }
    }
}
