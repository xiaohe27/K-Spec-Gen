package transform.ast;

/**
 * Created by hx312 on 13/10/2015.
 */
public abstract class KASTNode {
    private String name;
    private String kind;

    public KASTNode(String name) {
        this.name = name;
    }

    public abstract String toString();
}
