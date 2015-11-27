package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import parser.ExpressionParser;

import java.util.List;

/**
 * Created by hx312 on 26/11/2015.
 */
public class ParamsVisitor extends ASTVisitor {
    private List<SingleVariableDeclaration> params;

    public boolean visit(MethodDeclaration method) {
        System.out.println("Method " + method.getName() + " is visited!");

        if (this.params == null || this.params.isEmpty())
            this.params = method.parameters();

        return false;
    }

    public static CompilationUnit parseCUStr(String code) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(code.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        return ((CompilationUnit) parser.createAST(null));
    }

    public static List<SingleVariableDeclaration> extractParamsFromStr(String codeStr) {
        CompilationUnit cu = parseCUStr(codeStr);
        ParamsVisitor visitor = new ParamsVisitor();
        cu.accept(visitor);
        return visitor.params;
    }
}
