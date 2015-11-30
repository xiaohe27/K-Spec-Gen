package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;
import transform.utils.TypeMapping;
import transform.utils.Utils;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class EnvCell extends Cell {
    private HashMap<SimpleName, Integer> env; // var to val mapping

    public EnvCell(HashMap<SimpleName, Integer> env0) {
        super(Cell.ENV);
        this.env = env0;
        super.hasLeftOmission = true;
        super.hasRightOmission = true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (.Map => ?_:Map)\n");
        this.env.forEach((var, loc) -> {
            String varID = Utils.string2ID(var.getIdentifier());
            String locStr = "P" + loc + ":" + TypeMapping.getKBuiltInType4SimpleJType(var
                    .resolveTypeBinding().getName());
            sb.append(varID + " |-> " + locStr + "\n");
        });
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        return super.surroundWithTags(sb.toString());
    }
}
