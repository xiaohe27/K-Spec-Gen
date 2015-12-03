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
    private StringBuilder whileAST = new StringBuilder();
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
            this.whileAST.append("while ( ");
            //while loop's guard
            this.whileAST.append(KAST_Transformer.convert2KAST(guard, true));
            this.whileAST.append(" ) {\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void endVisit(WhileStatement whileNode) {
        this.whileAST.append("\n}\n");
    }

    public boolean visit(VariableDeclarationStatement vds) {
        StringBuilder sb = new StringBuilder();
        String type = Utils.convert2KAST_Type(vds.getType());
        String prefix = ".AnnoVarModList ";


        sb.append(prefix + type + " ");
        List<VariableDeclarationFragment> ids = vds.fragments();
        for (int i = 0; i < ids.size() - 1; i++) {
            String nameI = ids.get(i).getName().getIdentifier();
            sb.append(Utils.string2ID(nameI) + ", ");
        }

        sb.append(Utils.string2ID(ids.get(ids.size() - 1).getName().getIdentifier()));
        sb.append(" ;\n");
        this.whileAST.append(sb.toString());

        return false;
    }

    public boolean visit(Assignment assignmentNode) {

        Expression lhs = assignmentNode.getLeftHandSide();
        Expression rhs = assignmentNode.getRightHandSide();
        String op = assignmentNode.getOperator().toString();

        if (lhs instanceof QualifiedName)
            KAST_Transformer.lhsOfCurAssignIsQualifiedName = true;
        else
            KAST_Transformer.lhsOfCurAssignIsQualifiedName = false;

        try {

            String exprStr = "(" + KAST_Transformer.convert2KAST(lhs, false) + op + KAST_Transformer
                    .convert2KAST(rhs, true) + ")::AssignExp";

            String output = KAST_Transformer.cast2Type(exprStr, lhs.resolveTypeBinding());

            whileAST.append(output + " ; ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Set<SimpleName> getSetOfVarNames() {
        return this.varsInLoop;
    }

    public String getLoopASTString() {
        return this.whileAST.toString();
    }
}
