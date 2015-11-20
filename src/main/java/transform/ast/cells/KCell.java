package transform.ast.cells;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import transform.utils.TypeMapping;

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

        this.qualifiedClsName = this.methodInfo.getClassName();
        this.methodName = this.methodInfo.getMethodName();
        this.methArgs.addAll(this.methodInfo.getFormalParams());
        this.retVal = this.methodInfo.getRetVal();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.methodInfo.className2ID() + ".");
        sb.append(this.methodInfo.methodName2ID());
        sb.append(":Id(");

        for (int i = 0; i < this.methArgs.size() - 1; i++) {
            sb.append(TypeMapping.convert2KType(this.methArgs.get(i)) + ", ");
        }

        sb.append(TypeMapping.convert2KType(this.methArgs.get(this.methArgs.size() - 1)));
        sb.append(")\n");

        //TODO

        return sb.toString();
    }


}
