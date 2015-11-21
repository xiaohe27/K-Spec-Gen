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

    private final ArrayList<SingleVariableDeclaration> methArgs = new ArrayList<>();

    public KCell(MethodInfo methodInfo, LoopInfo loopInfo) {
        super(Cell.K);
        this.methodInfo = methodInfo;
        this.loopInfo = loopInfo;

        this.methArgs.addAll(this.methodInfo.getFormalParams());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.loopInfo == null) {
            sb.append(this.methodInfo.className2ID() + ".");
            sb.append(this.methodInfo.methodName2ID());
            sb.append(":Id(");

            for (int i = 0; i < this.methArgs.size() - 1; i++) {
                sb.append(TypeMapping.convert2KType(this.methArgs.get(i)) + ", ");
            }

            sb.append(TypeMapping.convert2KType(this.methArgs.get(this.methArgs.size() - 1)));
            sb.append(")\n");

            sb.append("==>\n");

            sb.append(this.methodInfo.getRetVal() + "::" + )
        }
        return sb.toString();
    }


}
