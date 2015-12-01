package parser.annotation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;
import parser.ExpressionParser;
import transform.ast.rewrite.KRewriteObj;
import transform.utils.TypeMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * The pos of the loop can be used in the process of gathering loop invariant from the annotation.
 */
public class LoopInfo {
    private final int startPos;
    private final int endPos;
    private ArrayList<Expression> loopInvs;
    private WhileStatement loopNode;

    private HashMap<String, String> rawEnvMap = new HashMap<>();
    private HashMap<String, String> rawStoreMap = new HashMap<>();

    public LoopInfo(int start, int len, WhileStatement lpNd) {
        this.loopInvs = new ArrayList<>();

        this.startPos = start;
        this.endPos = start + len;
        this.loopNode = lpNd;
    }

    public Expression get(int index) {
        return loopInvs.get(index);
    }

    public int size() {
        return loopInvs.size();
    }

    public int srcCodeSize() {
        return this.endPos - this.startPos;
    }

    public boolean isPosInside(int pos) {
        return pos >= startPos && pos < endPos;
    }

    public boolean addLI(String loopInvStr) {
        Expression li = ExpressionParser.parseExprStr(loopInvStr);
        return loopInvs.add(li);
    }

    public WhileStatement getLoopNode() {
        return this.loopNode;
    }

    public Stream<Expression> getLIStream() {
        return this.loopInvs.stream();
    }


    public void addEnvInfo(String envCellInfo) {
        Matcher matcher = Patterns.EnvEntry.matcher(envCellInfo);
        while (matcher.find()) {
            this.rawEnvMap.put(matcher.group(1).trim(), matcher.group(2).trim());
        }
    }

    public void addStoreInfo(String storeCellInfo) {
        Matcher matcher = Patterns.StoreEntry.matcher(storeCellInfo);
        while (matcher.find()) {
            this.rawStoreMap.put(matcher.group(1).trim(), matcher.group(2).trim());
        }
    }

    public void updateEnvMap(Set<SimpleName> namesInLoop, HashMap<SimpleName, Integer> envMap) {
        namesInLoop.stream().filter(var -> this.rawEnvMap.keySet().contains(var.getIdentifier()))
                .forEach(var -> {
                    envMap.put(var, Integer.valueOf(this.rawEnvMap.get(var.getIdentifier())));
                });
    }

    public void updateStoreMap(HashMap<SimpleName, Integer> envMap) {
        Set<SimpleName> localVars = envMap.keySet();

        this.rawStoreMap.keySet().stream()
                .filter(loc -> envMap.values().contains(Integer.valueOf(loc)))
                .forEach(locStr -> {
                    Integer loc = Integer.valueOf(locStr);
                    String valStr = this.rawStoreMap.get(locStr);
                    final String[] elements = valStr.split("=>");
                    for (int i = 0; i < elements.length; i++) {
                        Expression expI = ExpressionParser.parseExprStr(elements[i]);
                        //transform to k expr where every op has been transformed
                        elements[i] = TypeMapping.fromJExpr2KExprString(expI, localVars);
                    }
                    KRewriteObj kRewriteObj = new KRewriteObj()
        });
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Loop ranges in lines [" + startPos + ", " + endPos + "]\n");
        sb.append("LI of the loop is \n");
        loopInvs.forEach(li -> sb.append(li + "\n"));

        sb.append("Env of the loop is\n");
        this.rawEnvMap.forEach((k, v) -> {
                    sb.append(k + " |-> " + v + "\n");
                }
        );

        sb.append("Store of the loop is\n");
        this.rawStoreMap.forEach((k, v) -> {
            sb.append(k + " |-> " + v + "\n");
        });
        return sb.toString();
    }
}