package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import parser.annotation.AnnotationInfo;
import parser.annotation.LoopInfo;
import parser.annotation.MethodInfo;
import parser.annotation.Patterns;

/**
 * Created by hx312 on 30/09/2015.
 */
public class MyASTVisitor extends ASTVisitor {
    private final CompilationUnit cu;
    private final AnnotationInfo annotationInfo;
    private int curMethNodeId = -1;
    private String curPackageName = null;
    private String curClsName = null;
    /**
     * Tmp var for testing purpose.
     */
    private String srcCode;

    private MyASTVisitor(CompilationUnit cu) {
        this.cu = cu;
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
        this.curClsName = typeDeclaration.getName().getFullyQualifiedName();
        return true;
    }


    public boolean visit(WhileStatement whileNode) {
        if (this.curMethNodeId >= 0) {
            MethodInfo curMethod = this.annotationInfo.getMethodInfo(this.curMethNodeId);
            if (curMethod != null) {
                curMethod.addLoopInfo(new LoopInfo(whileNode.getStartPosition(),
                        whileNode.getLength(), whileNode));
            }
        }

        return true;
    }

    public boolean visit(MethodDeclaration methodNode) {
        this.curMethNodeId++;

        System.out.println("Method " + this.curClsName + "." + methodNode.getName()
                .getFullyQualifiedName() + " is visited!");

        String jdocStr = null;
        if (methodNode.getJavadoc() != null) {
            jdocStr = methodNode.getJavadoc().toString();
        }

        if (jdocStr != null && jdocStr.trim().matches(Patterns.METHOD_CONTRACT.pattern())) {
            MethodInfo methodInfo = new MethodInfo(
                    this.curClsName,
                    methodNode,
                    methodNode.getStartPosition(),
                    methodNode.getLength(),
                    jdocStr);

//            System.out.println(methodInfo.toString());
//            System.out.println(methodNode.getJavadoc().toString() + " is the javadoc!!!");
            this.annotationInfo.addMethod(this.curMethNodeId, methodInfo);
        }

        return true;
    }

    public boolean visit(ReturnStatement retStmt) {
        Expression retExpr = retStmt.getExpression();
        MethodInfo methodInfo = this.annotationInfo.getMethodInfo(this.curMethNodeId);
        if (methodInfo != null)
            methodInfo.setRetExpr(retExpr);
        return false;
    }

    public AnnotationInfo getAnnotationInfo() {
        return annotationInfo;
    }
}
