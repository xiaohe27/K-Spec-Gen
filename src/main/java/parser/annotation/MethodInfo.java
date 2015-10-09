package parser.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by xiaohe on 10/6/15.
 */
public class MethodInfo {
    private final String methName;
    private final int startPos;
    private final int endPos;

    private ArrayList<String> preCondList = new ArrayList<>();
    private ArrayList<String> postCondList = new ArrayList<>();
    private String retVal;

    /**
     * The index is the pos of the loop:
     * If in the ordinary traversal, the loop is the nth to be visited,
     * then its index is n.
     */
    private ArrayList<LoopInfo> loopsInfo = new ArrayList<>();


    public MethodInfo(String methName, int startPos, int len, String preAndPostCond) {
        this.methName = methName;
        this.startPos = startPos;
        this.endPos = startPos + len;

        parseMethodContract(preAndPostCond);
    }

    public boolean isInsideMethod(int pos) {
        return pos >= this.startPos && pos < this.endPos;
    }

    private void parseMethodContract(String preAndPostCond) {
        Matcher matcher = Patterns.METHOD_CONTRACT.matcher(preAndPostCond);
        int count = 0;
        int groupSize = matcher.groupCount();
        System.out.println("Group size is " + groupSize);

        String contractStr = null;
        if(matcher.find()) {
            contractStr = matcher.group(1);
        }

        //if the javadoc is not a method contract, then ignore it.
        if (contractStr == null) {
            System.out.println("No match for the method annotation is found.");
            return;
        }

        matcher = Patterns.SingleClause.matcher(contractStr);

        while (matcher.find()) {

            String category = matcher.group(1);

            switch (category) {
                case Patterns.REQUIRES:
//                    System.out.println("@pre : " + matcher.group(2));
                    this.preCondList.add(matcher.group(2));
                    break;

                case Patterns.ENSURES:
//                    System.out.println("@post : " + matcher.group(2));
                    this.postCondList.add(matcher.group(2));
                    break;

                case Patterns.RETURNS:
//                    System.out.println("@ret : " + matcher.group(2));
                    this.retVal = matcher.group(2);
                    break;

                default:
                    break;
            }
        }
    }

    public void addPotentialLI(String loopInv, int commentStartPos) {
        for (int i = 0; i < this.loopsInfo.size() - 1; i++) {
            LoopInfo curLoop = this.loopsInfo.get(i);
            LoopInfo nxtLoop = this.loopsInfo.get(i + 1);
            //if the LI is inside the current loop but not the next loop,
            //then it must be belong to the current loop.
            //this is true for both sequential and nested cases.
            if (curLoop.isPosInside(commentStartPos) &&
                    ! nxtLoop.isPosInside(commentStartPos)) {
                curLoop.addLI(loopInv);
                return;
            }
        }

        if (this.loopsInfo.size() > 0) {
            LoopInfo lastLoop = this.loopsInfo.get(this.loopsInfo.size() - 1);
            if (lastLoop.isPosInside(commentStartPos)) {
                lastLoop.addLI(loopInv);
            }
        }
    }

    public void addLoopInfo(LoopInfo loopInfo) {
        this.loopsInfo.add(loopInfo);
    }

    public LoopInfo getLoopInfo(int index) {
        return loopsInfo.get(index);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
               sb.append("Method " + methName + " 's contract is \n");

        this.preCondList.forEach(preCondStr -> {sb.append("@pre: " + preCondStr + ";\n");});
        this.postCondList.forEach(postCondStr -> {sb.append("@post: " + postCondStr + ";\n");});

        sb.append("@ret: " + this.retVal + ";\n");
        sb.append("\nLoop info is :\n");

        for (int index = 0; index < this.loopsInfo.size(); index++) {
            sb.append("Loop No. " + index + "'s info is :\n");
            sb.append(getLoopInfo(index));
        }

        return sb.toString();
    }
}
