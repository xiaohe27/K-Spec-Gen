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
    private final CompilationUnit cu;
    private final AnnotationInfo annotationInfo;
    Set names;
    private int curMethNodeId = 0;
    private String curPackageName = null;
    private String curClsName = null;
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

        this.names = new HashSet();

        this.annotationInfo = new AnnotationInfo();
    }

    public boolean visit(PackageDeclaration packageDeclaration) {
        this.curPackageName = packageDeclaration.getName().getFullyQualifiedName();

        System.out.println("Visit package" + this.curPackageName);
        return true;
    }

    public boolean visit(TypeDeclaration typeDeclaration) {
        this.curClsName = typeDeclaration.getName().getFullyQualifiedName();
        return true;
    }


    public boolean visit(WhileStatement whileNode) {
        if (this.curMethNodeId > 0) {
            MethodInfo curMethod = this.annotationInfo.getMethodInfo(this.curMethNodeId - 1);
            if (curMethod != null) {
                curMethod.addLoopInfo(new LoopInfo(whileNode.getStartPosition(),
                        whileNode.getLength(), whileNode));
            }
        }

        return true;
    }

    public boolean visit(MethodDeclaration methodNode) {
        System.out.println("Method " + this.curClsName + "." + methodNode.getName()
                .getFullyQualifiedName() + " is visited!");

        if (methodNode.getJavadoc() != null) {
            MethodInfo methodInfo = new MethodInfo(this.curClsName, methodNode,
                    methodNode.getStartPosition(),
                    methodNode.getLength(),
                    methodNode.getJavadoc().toString());

            System.out.println(methodInfo.toString());
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
