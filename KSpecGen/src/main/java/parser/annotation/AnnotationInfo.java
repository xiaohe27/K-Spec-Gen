package parser.annotation;

import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by xiaohe on 10/6/15.
 * Assume the traversal order is the same, then the node id would be the same for each program
 * construct. So the order can be used to associate two AST.
 */
public class AnnotationInfo {

    public enum LoopPropKind {LI, LoopPre, LoopPost};

    private HashMap<Integer, MethodInfo> methodsInfo = new HashMap<>();

    public boolean isNOTEmpty() {
        return methodsInfo.isEmpty() == false;
    }

    public void addMethod(int index, MethodInfo methodInfo) {
        this.methodsInfo.put(index, methodInfo);
    }

    //The line comment may be a LOOP_PROP if its pos is inside some loop.
    public void addPotentialLI(LoopPropKind kind, String loopInv, int commentStartPos) {
        methodsInfo.keySet().forEach(
                index -> {
                    MethodInfo curMethod = methodsInfo.get(index);
                    if (curMethod.isInsideMethod(commentStartPos)) {
                        curMethod.addPotentialLI(kind, loopInv, commentStartPos);
                        return;
                    }
                }
        );
    }

    //The line comment may be a LOOP_PROP if its pos is inside some loop.
    public void addEnvCellInfo(String envCellInfo, int commentStartPos) {
        methodsInfo.values().stream()
                .filter(curMethod -> curMethod.isInsideMethod(commentStartPos))
                .forEach(targetMethod -> {
                    targetMethod.addEnvMap(envCellInfo, commentStartPos);
                    return;
                });
    }

    public void addStoreCellInfo(String storeCellInfo, int commentStartPos) {
        methodsInfo.values().stream()
                .filter(curMethod -> curMethod.isInsideMethod(commentStartPos))
                .forEach(targetMethod -> {
                    targetMethod.addStoreMap(storeCellInfo, commentStartPos);
                    return;
                });
    }

    public void addObjStoreCellInfo(String objStoreContent, int commentStartPos) {
        methodsInfo.values().stream()
                .filter(curMethod -> curMethod.isInsideMethod(commentStartPos))
                .forEach(targetMethod -> {
                    targetMethod.addObjStoreInfo4Loop(objStoreContent, commentStartPos);
                    return;
                });
    }

    public MethodInfo getMethodInfo(int index) {
        return methodsInfo.get(index);
    }

    public Stream<MethodInfo> getMethodsInfoStream() {
        return methodsInfo.values().stream();
    }

    public void printInfo() {
        //method info
        methodsInfo.keySet().forEach(index -> System.out.println(
                "Method No." + index + ":\t" + getMethodInfo(index).toString()));
    }
}
