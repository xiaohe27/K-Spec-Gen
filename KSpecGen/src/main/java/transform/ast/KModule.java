package transform.ast;

import parser.annotation.MethodInfo;
import transform.ast.rewrite.KRewriteObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KModule extends KASTNode {
    private ArrayList<KImport> imports = new ArrayList<>();
    private HashMap<Integer, KRewriteObj> store = new HashMap<>();
    private ArrayList<KRule> rules = new ArrayList<>();

    public KModule(MethodInfo methodInfo) {
        super(methodInfo.getKModuleName());
        imports.add(new KImport());
        this.rules.addAll(getRulesFromLoops(methodInfo));
        this.rules.add(new KRule(methodInfo, this.store));
    }


    private ArrayList<KRule> getRulesFromLoops(MethodInfo methodInfo) {
        ArrayList<KRule> rules = new ArrayList<>();

        for (int i = 0; i < methodInfo.getNumOfLoops(); i++) {
            rules.add(new KRule(methodInfo, methodInfo.getLoopInfo(i), this.store));
        }
        return rules;
    }

    private String printImports() {
        StringBuilder sb = new StringBuilder();
        this.imports.forEach(importClause -> sb.append(importClause.toString() + "\n"));
        return sb.toString();
    }

    private String printRules() {
        StringBuilder sb = new StringBuilder();
        this.rules.forEach(ruleClause -> sb.append(ruleClause.toString() + "\n"));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("module ");
        sb.append(super.name);
        sb.append("\n");
        sb.append(printImports() + "\n");
        sb.append(printRules() + "\n");
        sb.append("endmodule\n");

        return sb.toString();
    }
}
