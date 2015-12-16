package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;
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
        if (this.env == null || this.env.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" (.Map => ?_:Map)" + Utils.NEW_LINE);
        this.env.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue() - entry2.getValue())
                .forEach(entry -> {
                    String varID = Utils.string2ID(entry.getKey().getIdentifier());
                    String locStr = "P" + entry.getValue() + ":Int";
                    sb.append(varID + " |-> " + locStr + Utils.NEW_LINE);
                });
        sb.deleteCharAt(sb.lastIndexOf(Utils.NEW_LINE));
        return super.surroundWithTags(sb.toString());
    }
}
