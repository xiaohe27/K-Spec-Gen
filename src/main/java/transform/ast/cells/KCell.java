package transform.ast.cells;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 31/10/2015.
 */
public class KCell extends Cell {
    private final MethodInfo methodInfo;

    private final LoopInfo loopInfo;

    private final String qualifiedClsName;
    private final String methodName;
    private final ArrayList<SingleVariableDeclaration> methArgs = new ArrayList<>();
    private String retVal;

    public KCell(MethodInfo methodInfo, LoopInfo loopInfo) {
        super(Cell.K);
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;

        this.qualifiedClsName = this.methodInfo.getQualifiedName();
        this.methodName = this.methodInfo.getMethodName();
        this.methArgs.addAll(this.methodInfo.getFormalParams());
        this.retVal = this.methodInfo.getRetVal();
    }


}
