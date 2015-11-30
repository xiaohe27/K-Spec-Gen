package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import transform.utils.KAST_Transformer;
import transform.utils.TypeMapping;

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
        System.out.println("KAST is \n");

        Expression lhs = assignmentNode.getLeftHandSide();
        Expression rhs = assignmentNode.getRightHandSide();
        String op = assignmentNode.getOperator().toString();

        try {
            String exprStr = "(" + KAST_Transformer.convert2KAST(lhs, false) + op + KAST_Transformer
                    .convert2KAST(rhs, true) + ")::AssignExp";

            String type = lhs.resolveTypeBinding().toString();
            type = type.equals("boolean") ? "bool" : type;
            String output = KAST_Transformer.cast2Type(exprStr, type);

            System.out.println(output);

            kAST.append(output + " ; ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Stream<SimpleName> getStreamOfVarNames() {
        return this.varsInLoop.stream();
    }
}
