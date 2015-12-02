package parser.ast_visitor;

import org.eclipse.jdt.core.dom.*;
import transform.utils.KAST_Transformer;
import transform.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hx312 on 28/11/2015.
 */
public class LoopVisitor extends ASTVisitor {
    private String whileGuardAST;
    private StringBuilder whileBodyAST = new StringBuilder();
    private Set<SimpleName> varsInLoop = new HashSet<>();

    public boolean visit(SimpleName name) {
        for (SimpleName curName :
                varsInLoop) {
            if (curName.getIdentifier().equals(name.getIdentifier())) {
                return false;
            }
        }

        this.varsInLoop.add(name);
        return false;
    }

    public boolean visit(WhileStatement whileNode) {
        Expression guard = whileNode.getExpression();
        try {
            this.whileGuardAST = KAST_Transformer.convert2KAST(guard, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean visit(VariableDeclarationStatement vds) {
        StringBuilder sb = new StringBuilder();
        String type = vds.getType().toString();
        String prefix = ".AnnoVarModList ";
        if (vds.getType().isPrimitiveType()) {
            type = type.equals("boolean") ? "bool" : type;
        } else {
            type = "class ." + type;
        }

        sb.append(prefix + type + " ");
        List<VariableDeclarationFragment> ids = vds.fragments();
        for (int i = 0; i < ids.size() - 1; i++) {
            String nameI = ids.get(i).getName().getIdentifier();
            sb.append(Utils.string2ID(nameI) + ", ");
        }

        sb.append(Utils.string2ID(ids.get(ids.size() - 1).getName().getIdentifier()));
        sb.append(" ;\n");
        this.whileBodyAST.append(sb.toString());

        return false;
    }

    public boolean visit(Assignment assignmentNode) {

        Expression lhs = assignmentNode.getLeftHandSide();
        Expression rhs = assignmentNode.getRightHandSide();
        String op = assignmentNode.getOperator().toString();

        try {
            String exprStr = "(" + KAST_Transformer.convert2KAST(lhs, false) + op + KAST_Transformer
                    .convert2KAST(rhs, true) + ")::AssignExp";

            String type = lhs.resolveTypeBinding().toString();
            type = type.equals("boolean") ? "bool" : type;
            String output = KAST_Transformer.cast2Type(exprStr, type);

            whileBodyAST.append(output + " ; ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Set<SimpleName> getSetOfVarNames() {
        return this.varsInLoop;
    }

    public String getLoopASTString() {
        StringBuilder sb = new StringBuilder();
        sb.append("while ( ");
        sb.append(this.whileGuardAST);
        sb.append(" ) {\n");
        sb.append(this.whileBodyAST);
        sb.append("}\n");

        return sb.toString();
    }
}
