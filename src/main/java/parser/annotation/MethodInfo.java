package parser.annotation;

import java.util.HashMap;

/**
 * Created by xiaohe on 10/6/15.
 */
public class MethodInfo {
    private String methName;
    private String preCondStr;
    private String postCondStr;

    /**
     * The index is the pos of the loop:
     * If in the ordinary traversal, the loop is the nth to be visited,
     * then its index is n.
     */
    private HashMap<Integer, LoopInfo> loopsInfo = new HashMap<>();


    public MethodInfo(String methName, String preAndPostCond) {
        this.methName = methName;
        parseMethodContract(preAndPostCond);
    }

    private void parseMethodContract(String preAndPostCond) {

    }

    public void addLoopInfo(int index, LoopInfo loopInfo) {
        this.loopsInfo.put(index, loopInfo);
    }

    public LoopInfo getLoopInfo(int index) {
        return loopsInfo.get(index);
    }


    public String toString() {
        String ret = "Method " + methName + " 's contract is \n";
        ret += "pre-condition: " + this.preCondStr;
        ret += "post-condition: " + this.postCondStr;
        ret += "\nLoop info is :\n";

        for (Integer index : loopsInfo.keySet()) {
            ret += "Loop No. " + index + "'s info is :\n";
            ret += getLoopInfo(index);
        }

        return ret;
    }
}
