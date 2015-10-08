package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import parser.annotation.AnnotationInfo;
import parser.annotation.MethodInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hx312 on 30/09/2015.
 */
public class MyASTVisitor extends ASTVisitor {
    private int curMethNodeId;
    private int curLoopId;

    private final CompilationUnit cu;
    Set names;

    private final AnnotationInfo annotationInfo;

    public MyASTVisitor(CompilationUnit cu) {
        this.cu = cu;
        names = new HashSet();

        this.annotationInfo = new AnnotationInfo();
    }

//    public boolean visit(VariableDeclarationFragment node) {
//		SimpleName name = node.getName();
//		this.names.add(name.getIdentifier());
//		System.out.println("Declaration of '" + name + "' at line"
//				+ cu.getLineNumber(name.getStartPosition()));
//		return false; // do not continue
//	}
//
//    public boolean visit(SimpleName node) {
//		if (this.names.contains(node.getIdentifier())) {
//			System.out.println("Usage of '" + node + "' at line "
//					+ cu.getLineNumber(node.getStartPosition()));
//		}
//		return true;
//	}

    public boolean visit(MethodDeclaration methodNode) {
        if (methodNode.getJavadoc() != null) {
            MethodInfo methodInfo = new MethodInfo(methodNode.getName().toString(),
                    methodNode.getJavadoc().toString());

//            System.out.println(methodNode.getJavadoc().toString() + " is the javadoc!!!");
            this.annotationInfo.addMethod(this.curMethNodeId, methodInfo);
        }

        this.curMethNodeId++;
        return true;
    }

    public AnnotationInfo getAnnotationInfo() {
        return annotationInfo;
    }
}
