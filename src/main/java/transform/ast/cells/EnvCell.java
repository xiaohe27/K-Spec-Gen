package transform.ast.cells;

import org.eclipse.jdt.core.dom.SimpleName;

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
        sb.append("\n");
        this.env.forEach((var, loc) -> {
            sb.append(var + " |-> " + loc + "\n");
        });
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        return super.surroundWithTags(sb.toString());
    }
}
