package transform.ast;

import parser.annotation.MethodInfo;
import transform.ast.rewrite.KRewriteObj;
import transform.utils.Utils;

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
        this.imports.forEach(importClause -> sb.append(importClause.toString() + Utils.NEW_LINE));
        return sb.toString();
    }

    private String printRules() {
        StringBuilder sb = new StringBuilder();
        this.rules.forEach(ruleClause -> sb.append(ruleClause.toString() + Utils.NEW_LINE));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("module ");
        sb.append(super.name);
        sb.append(Utils.NEW_LINE);
        sb.append(printImports() + Utils.NEW_LINE);
        sb.append(printRules() + Utils.NEW_LINE);
        sb.append("endmodule" + Utils.NEW_LINE);

        return sb.toString();
    }
}
