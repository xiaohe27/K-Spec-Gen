package parser.annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by xiaohe on 10/6/15.
 * Assume the traversal order is the same, then the node id would be the same for each program
 * construct. So the order can be used to associate two AST.
 */
public class AnnotationInfo {
    private HashMap<Integer, MethodInfo> methodsInfo = new HashMap<>();


    public void addMethod(int index, MethodInfo methodInfo) {
        this.methodsInfo.put(index, methodInfo);
    }

    //The line comment may be a LI if its pos is inside some loop.
    public void addPotentialLI(String loopInv, int commentStartPos) {
        methodsInfo.keySet().forEach(
                index -> {
                    MethodInfo curMethod = methodsInfo.get(index);
                    if (curMethod.isInsideMethod(commentStartPos)) {
                        curMethod.addPotentialLI(loopInv, commentStartPos);
                        return;
                    }
                }
        );

    }

    public MethodInfo getMethodInfo(int index) {
        return methodsInfo.get(index);
    }

    public Collection<MethodInfo> getAllMethodsInfo() {
        return methodsInfo.values();
    }

    public void printInfo() {
        //method info
        methodsInfo.keySet().forEach(index -> System.out.println(
                "Method No." + index + ":\t" + getMethodInfo(index).toString()));
    }
}
