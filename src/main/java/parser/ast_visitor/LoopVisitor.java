package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by hx312 on 28/11/2015.
 */
public class LoopVisitor extends ASTVisitor {
    private StringBuilder kAST = new StringBuilder();
    private Set<SimpleName> varsInLoop = new HashSet<>();

    public boolean visit(SimpleName name) {
        this.varsInLoop.add(name);
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

    public Stream<SimpleName> getStreamOfVarNames() {
        return this.varsInLoop.stream();
    }
}
