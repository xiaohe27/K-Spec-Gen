package transform.ast;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KReqClause extends KASTNode {
    public static final String KReqFile = "\"java-verification.k\"";
    private final String reqFileName;

    public KReqClause() {
        this(null);
    }

    public KReqClause(String reqFileName) {
        super("'KRequireClause");
        this.reqFileName = reqFileName == null ? KReqFile : reqFileName;
    }

    @Override
    public String toString() {
        return "require " + this.reqFileName;
    }
}
