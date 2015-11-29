package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;

/**
 * Created by hx312 on 28/11/2015.
 */
public class LoopVisitor extends ASTVisitor {
    private StringBuilder kAST = new StringBuilder();

    public boolean visit(SimpleName name) {
        //TODO
        IBinding binding = name.resolveTypeBinding();

        if (binding != null)
            System.out.println(binding.getName() + " is the assoc binding for " + name.getIdentifier());

        return false;
    }

    public boolean visit(WhileStatement whileNode) {
        //TODO
        return true;
    }

    public boolean visit(Assignment assignmentNode) {
        //TODO
        System.out.println("Visit assignment stmt: " + assignmentNode.toString());
        return true;
    }


}
