package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;

/**
 * Created by xiaohe on 9/30/15.
 */
public class CommentVisitor extends ASTVisitor {
    private CompilationUnit cu;
    private String source;

    public CommentVisitor(String pgmTxt) {
        super();
        this.source = pgmTxt;
    }

    public boolean visit(LineComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
        String comment = source.substring(start, end);

        System.out.println("Start pos : " + start + ";\n End pos: " + end);
        System.out.println(comment);

        System.out.println("The parent node is " + node.getRoot());
        return true;
    }

    public boolean visit(BlockComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
        String comment = source.substring(start, end);

        System.out.println("Start pos : " + start + ";\n End pos: " + end);
        System.out.println(comment);
        return true;
    }
}
