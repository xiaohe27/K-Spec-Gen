package parser.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by xiaohe on 10/6/15.
 */
public class MethodInfo {
    private String methName;
    private ArrayList<String> preCondList = new ArrayList<>();
    private ArrayList<String> postCondList = new ArrayList<>();
    private String retVal;

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

    public void addLoopInfo(int index, LoopInfo loopInfo) {
        this.loopsInfo.put(index, loopInfo);
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

        for (Integer index : loopsInfo.keySet()) {
            sb.append("Loop No. " + index + "'s info is :\n");
            sb.append(getLoopInfo(index));
        }

        return sb.toString();
    }
}
