package transform.ast.cells;

import transform.ast.KASTNode;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class Cell extends KASTNode {
    public static final String HOLDS = "holds";
    public static final String CLASSES = "classes";
    public static final String NumOfClassesToUnfold = "NumOfClassesToUnfold";
    public static final String PROGRAM = "program";
    public static final String GlobalPhase = "globalPhase";
    public static final String BUSY = "busy";
    public static final String NEXT_LOC = "nextLoc";
    //the name of cells which need to be created carefully
    public static final String THREADS = "threads";
    public static final String THREAD = "thread";
    public static final String K = "k";
    public static final String ENV = "env";
    public static final String STORE = "store";
    public static final String STORE_METADATA = "storeMetadata";
    public static final String OBJ_STORE = "objectStore";
    protected boolean hasLeftOmission = false;
    protected boolean hasRightOmission = false;
    protected ArrayList<Cell> childrenCells;

    public Cell(String name) {
        super(name);
        this.childrenCells = new ArrayList<>();
    }


    public static Cell getFixedCellWithName(String cellName) {
        return new Cell(cellName);
    }

    protected String surroundWithTags(String content) {
        return "<" + this.name + ">\n"
                + (this.hasLeftOmission ? " ... " : "")
                + content + "\n"
                + (this.hasRightOmission ? " ... " : "")
                + "</" + this.name + ">\n";
    }

    @Override
    public String toString() {
        switch (this.name) {
            case HOLDS:
                return surroundWithTags(" .Map ");

            case CLASSES:
                return surroundWithTags(" CLASSES:Bag ");

            case NumOfClassesToUnfold:
                return surroundWithTags(" 0 ");

            case PROGRAM:
                return surroundWithTags(" .K ");

            case GlobalPhase:
                return surroundWithTags(" ExecutionPhase ");

            case BUSY:
                return surroundWithTags(" .Set ");

            case NEXT_LOC:
                return surroundWithTags(" I:Int => ?_:Int ");

            default:
                StringBuilder sb = new StringBuilder();
                this.childrenCells.forEach(childCell -> sb.append(childCell.toString() + "\n"));
                return surroundWithTags(sb.toString());
        }

    }
}
