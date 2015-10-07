package parser.annotation;

import java.util.ArrayList;

/**
 * The pos of the loop can be used in the process of gathering loop invariant from the annotation.
 */
public class LoopInfo {
    private ArrayList<String> loopInvs;

    private final int startPos;
    private final int endPos;

    public String get(int index) {
        return loopInvs.get(index);
    }

    public int size() {
        return loopInvs.size();
    }

    public boolean add(String s) {
        return loopInvs.add(s);
    }

    public LoopInfo(int start, int end) {
        this.loopInvs = new ArrayList<>();

        this.startPos = start;
        this.endPos = end;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Loop ranges in lines [" + startPos + ", " + endPos + "]\n");
        sb.append("LI of the loop is \n");
        loopInvs.forEach(li -> sb.append(li + "\n"));

        return sb.toString();
    }


}