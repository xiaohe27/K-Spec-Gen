package transform.ast;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KRule extends KASTNode {
    private ArrayList<KCondition> preConds;
    private ArrayList<KCondition> postConds;
    private ArrayList<KCell> cells;

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo) {
        super("'KRule");

    }

    public KRule(MethodInfo methodInfo) {
        super("'KRule");
    }

    @Override
    public String toString() {
        return null;
    }
}
