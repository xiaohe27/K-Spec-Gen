package parser.annotation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.WhileStatement;
import parser.ExpressionParser;

import java.util.ArrayList;

/**
 * The pos of the loop can be used in the process of gathering loop invariant from the annotation.
 */
public class LoopInfo {
    private final int startPos;
    private final int endPos;
    private ArrayList<Expression> loopInvs;
    private WhileStatement loopNode;

    public LoopInfo(int start, int len, WhileStatement lpNd) {
        this.loopInvs = new ArrayList<>();

        this.startPos = start;
        this.endPos = start + len;
        this.loopNode = lpNd;
    }

    public Expression get(int index) {
        return loopInvs.get(index);
    }

    public int size() {
        return loopInvs.size();
    }

    public boolean isPosInside(int pos) {
        return pos >= startPos && pos < endPos;
    }

    public boolean addLI(String loopInvStr) {
        System.out.println("The loopInvStr is " + loopInvStr);
        Expression li = ExpressionParser.parseExprStr(loopInvStr);
        return loopInvs.add(li);
    }

    public WhileStatement getLoopNode() {
        return this.loopNode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Loop ranges in lines [" + startPos + ", " + endPos + "]\n");
        sb.append("LI of the loop is \n");
        loopInvs.forEach(li -> sb.append(li + "\n"));

        return sb.toString();
    }


}