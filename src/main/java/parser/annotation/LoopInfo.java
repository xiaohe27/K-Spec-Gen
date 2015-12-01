package parser.annotation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.WhileStatement;
import parser.ExpressionParser;
import parser.ast_visitor.LoopVisitor;
import transform.ast.KCondition;
import transform.ast.rewrite.KRewriteObj;
import transform.utils.TypeMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final LoopVisitor loopVisitor = new LoopVisitor();

    private HashMap<String, String> rawEnvMap = new HashMap<>();
    private HashMap<String, String> rawStoreMap = new HashMap<>();

    public LoopInfo(int start, int len, WhileStatement lpNd) {
        this.loopInvs = new ArrayList<>();

        this.startPos = start;
        this.endPos = start + len;
        this.loopNode = lpNd;

        this.loopNode.accept(this.loopVisitor);
        //after the loop visitor collects info from the while loop, the vars used in the loop
        // will be known, and then we can construct the real env.
    }

    public String getLoopASTString() {
        return loopVisitor.getLoopASTString();
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

    /**
     * Update the env map and store map.
     *
     * @param envMap   The environment map being updated.
     * @param storeMap The store map being updated.
     */
    public void updateEnvAndStore(HashMap<SimpleName, Integer> envMap,
                                  HashMap<Integer, KRewriteObj> storeMap) {
        if (envMap == null || storeMap == null) {
            throw new RuntimeException("env/store maps haven't been initialized before updating");
        }

        updateEnvMap(this.loopVisitor.getStreamOfVarNames(), envMap);
        updateStoreMap(envMap, storeMap);
    }

    private void updateEnvMap(Stream<SimpleName> namesInLoop, HashMap<SimpleName, Integer> envMap) {
        namesInLoop.filter(var -> this.rawEnvMap.keySet().contains(var.getIdentifier()))
                .forEach(var -> {
                    envMap.put(var, Integer.valueOf(this.rawEnvMap.get(var.getIdentifier())));
                });
    }

    /**
     * This method should only be called after updateEnvMap.
     */
    private void updateStoreMap(HashMap<SimpleName, Integer> envMap,
                                HashMap<Integer, KRewriteObj> storeMap) {
        Set<SimpleName> localVars = envMap.keySet();

        envMap.entrySet().stream()
                .filter(entry -> this.rawStoreMap.keySet().contains(entry.getValue().toString()))
                .forEach(envEntry -> {
                    SimpleName name = envEntry.getKey();
                    Integer loc = envEntry.getValue();
                    String valStr = this.rawStoreMap.get(loc.toString());
                    final String[] elements = valStr.split("=>");
                    for (int i = 0; i < elements.length; i++) {
                        Expression expI = ExpressionParser.parseExprStr(elements[i]);
                        //transform to k expr where every op has been transformed
                        elements[i] = TypeMapping.fromJExpr2KExprString(expI, localVars);
                    }

                    KRewriteObj kRewriteObj = new KRewriteObj(name.resolveTypeBinding(),
                            elements[0],
                            elements.length == 2 ? elements[1] : null);
                    storeMap.put(loc, kRewriteObj);
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