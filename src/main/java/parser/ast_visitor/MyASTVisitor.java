package parser.ast_visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hx312 on 30/09/2015.
 */
public class MyASTVisitor extends ASTVisitor {

    private final CompilationUnit cu;
    Set names;

    public MyASTVisitor(CompilationUnit cu) {
        this.cu = cu;
        names = new HashSet();
    }

    public boolean visit(VariableDeclarationFragment node) {
		SimpleName name = node.getName();
		this.names.add(name.getIdentifier());
		System.out.println("Declaration of '" + name + "' at line"
				+ cu.getLineNumber(name.getStartPosition()));
		return false; // do not continue
	}

    public boolean visit(SimpleName node) {
		if (this.names.contains(node.getIdentifier())) {
			System.out.println("Usage of '" + node + "' at line "
					+ cu.getLineNumber(node.getStartPosition()));
		}
		return true;
	}
}
