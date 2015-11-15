package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import parser.annotation.AnnotationInfo;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hx312 on 30/09/2015.
 */
public class MyASTVisitor extends ASTVisitor {
    private int curMethNodeId = 0;
    private String curPackageName = null;
    private String curClsName = null;

    private final CompilationUnit cu;
    Set names;

    private final AnnotationInfo annotationInfo;

    /**
     * Tmp var for testing purpose.
     */
    private String srcCode;

    private MyASTVisitor(CompilationUnit cu) {
        this.cu = cu;
        names = new HashSet();

        this.annotationInfo = new AnnotationInfo();
    }

    public MyASTVisitor(CompilationUnit cu, String srcCode) {
        this.cu = cu;
        this.srcCode = srcCode;
        this.annotationInfo = new AnnotationInfo();
    }

    public boolean visit(PackageDeclaration packageDeclaration) {
        this.curPackageName = packageDeclaration.getName().getFullyQualifiedName();

        System.out.println("Visit package" + this.curPackageName);
        return true;
    }

    public boolean visit(TypeDeclaration typeDeclaration) {
        System.out.println("decl type " + typeDeclaration.toString());

        return true;
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

    public boolean visit(WhileStatement whileNode) {
//        System.out.println("While loop from ast:\n" + whileNode.toString());
//
//        System.out.println("Construct the loop from start and len: ");
//        System.out.println(this.srcCode.substring(whileNode.getStartPosition(),
//                whileNode.getStartPosition() + whileNode.getLength()));

//        System.out.println("its condition: " + whileNode.getExpression().toString() + "\n");

        if (this.curMethNodeId > 0) {
            MethodInfo curMethod = this.annotationInfo.getMethodInfo(this.curMethNodeId - 1);
            if (curMethod != null) {
                curMethod.addLoopInfo(new LoopInfo(whileNode.getStartPosition(),
                        whileNode.getLength()));
            }
        }

        return true;
    }

    public boolean visit(SingleMemberAnnotation condition) {
        System.out.println("The annotation of " + condition.getParent().toString() + " is " + condition
                .toString());

//        ITypeBinding binding = condition.resolveTypeBinding();
//        if (binding != null) {
//            System.out.println("Type binding:");
//            System.out.println(binding);
//        }
//
//        IAnnotationBinding annotationBinding = condition.resolveAnnotationBinding();
//        if (annotationBinding != null) {
//            System.out.println("Annotation binding is ");
//            System.out.println(annotationBinding);
//        }

        Expression exp = condition.getValue();
        System.out.println("Exp of this annotation is " + exp);
        if (exp instanceof InfixExpression) {
            System.out.println("It is infix exp whose operator is " + ((InfixExpression) exp)
                    .getOperator());
        }

        return true;
    }

    public boolean visit(MethodDeclaration methodNode) {
        System.out.println("Method " + methodNode.getName() + " is visited!");

        if (methodNode.getJavadoc() != null) {
            MethodInfo methodInfo = new MethodInfo(methodNode,
                    methodNode.getStartPosition(),
                    methodNode.getLength(),
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
