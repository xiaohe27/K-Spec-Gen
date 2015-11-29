package parser.ast_visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Created by hx312 on 28/11/2015.
 */
public class LoopVisitor extends ASTVisitor {
    public boolean visit(SimpleName name) {
        IBinding binding = name.resolveTypeBinding();

        if (binding != null)
            System.out.println(binding.getName() + " is the assoc binding for " + name.getIdentifier());

        return false;
    }
}
