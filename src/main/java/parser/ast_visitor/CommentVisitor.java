package parser.ast_visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LineComment;

/**
 * Created by xiaohe on 9/30/15.
 */
public class CommentVisitor extends ASTVisitor {
    private CompilationUnit cu;
    private String source;

    public CommentVisitor(CompilationUnit cu, String pgmTxt) {
        super();
        this.cu = cu;
        this.source = pgmTxt;
    }

    public boolean visit(LineComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
        String comment = source.substring(start, end);
        System.out.println(comment);
        return true;
    }

    public boolean visit(BlockComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
        String comment = source.substring(start, end);
        System.out.println(comment);
        return true;
    }
}
