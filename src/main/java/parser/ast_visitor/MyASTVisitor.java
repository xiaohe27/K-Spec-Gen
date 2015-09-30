package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;

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

    public boolean visit(LineComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
//        String comment = source.substring(start, end);
//        System.out.println(comment);
        System.out.println("visit comment line " + node.toString());
        return true;
    }

    public boolean visit(BlockComment node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
//        String comment = source.substring(start, end);
//        System.out.println(comment);
        return true;
    }

    public boolean visit(Javadoc node) {
        int start = node.getStartPosition();
        int end = start + node.getLength();
        System.out.println(node);
        return true;
    }
}
