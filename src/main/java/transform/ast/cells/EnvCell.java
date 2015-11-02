package transform.ast.cells;

import java.util.HashMap;

/**
 * Created by hx312 on 31/10/2015.
 */
public class EnvCell extends Cell {
    private HashMap<String, String> env; // var to val mapping

    public EnvCell(HashMap<String, String> env) {
        super(Cell.ENV);
        this.env = env;
    }
}
