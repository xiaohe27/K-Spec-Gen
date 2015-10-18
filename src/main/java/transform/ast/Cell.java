package transform.ast;

import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

/**
 * Created by hx312 on 13/10/2015.
 */
public class Cell extends KASTNode {
    public static final String Type_K = "'k";

    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    private Cell(String name, MethodInfo methodInfo, LoopInfo loopInfo) {
        super(name);
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;
    }

    public static Cell getKCell4Method(MethodInfo methodInfo) {
        return new Cell(Type_K, methodInfo, null);
    }

    public static Cell getKCell4Loop(MethodInfo methodInfo, LoopInfo loopInfo) {
        return new Cell(Type_K, methodInfo, loopInfo);
    }

    @Override
    public String toString() {
        return null;
    }
}
