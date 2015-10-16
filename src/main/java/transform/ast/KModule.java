package transform.ast;

import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KModule extends KASTNode {
    private ArrayList<KImport> imports;

    private ArrayList<KRule> rules;

    public KModule(MethodInfo methodInfo) {
        super(methodInfo.getMethodName());
        imports.add(new KImport());
        this.rules.add(getMethodContractRule(methodInfo));
        this.rules.addAll(getRulesFromLoops(methodInfo));
    }

    private static KRule getMethodContractRule(MethodInfo methodInfo) {
        return new KRule(methodInfo);
    }


    private static ArrayList<KRule> getRulesFromLoops(MethodInfo methodInfo) {
        ArrayList<KRule> rules = new ArrayList<>();

        for (int i = 0; i < methodInfo.getNumOfLoops(); i++) {
            rules.add(new KRule(methodInfo, methodInfo.getLoopInfo(i)));
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
