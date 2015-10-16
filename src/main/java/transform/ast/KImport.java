package transform.ast;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KImport extends KASTNode {
    public static final String defaultImport = "JAVA-VERIFICATION";

    public KImport() {
        super("'KImport");
    }

    @Override
    public String toString() {
        return "imports " + defaultImport;
    }
}
