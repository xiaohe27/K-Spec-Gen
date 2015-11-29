package parser.ast_visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Created by hx312 on 28/11/2015.
 */
public class LoopVisitor extends ASTVisitor {

    @Override
    public void preVisit(ASTNode node) {
        System.out.println("Before visit " + node.toString());
    }
}
