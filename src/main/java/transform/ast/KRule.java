package transform.ast;

import org.eclipse.jdt.core.dom.Expression;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KRule extends KASTNode {
    private ArrayList<KCondition> preConds;
    private ArrayList<KCondition> postConds;
    private ArrayList<KCell> cells;

    public KRule(MethodInfo methodInfo, LoopInfo loopInfo) {
        super("'KRule");
        this.preConds.addAll(extractAllPreCond(methodInfo.getPreCondList()));
        this.postConds.addAll(extractAllPostCond(methodInfo.getPostCondList()));

    }

    public KRule(MethodInfo methodInfo) {
        this(methodInfo, null);
    }

    private Collection<? extends KCondition> extractAllPostCond(ArrayList<Expression> postCondList) {
        return null;
    }

    private Collection<? extends KCondition> extractAllPreCond(ArrayList<Expression> preCondList) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
