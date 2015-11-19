package parser.annotation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.ExpressionParser;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by xiaohe on 10/6/15.
 */
public class MethodInfo {
    private final String className;
    private final MethodDeclaration methodDecl;
    private final int startPos;
    private final int endPos;

    //The pre and post condition list are extracted from the method contract.
    private ArrayList<Expression> preCondList = new ArrayList<>();
    private ArrayList<Expression> postCondList = new ArrayList<>();
    //the expected return value according to the method contract.
    private String retVal;

    /**
     * The index is the pos of the loop:
     * If in the ordinary traversal, the loop is the nth to be visited,
     * then its index is n.
     */
    private ArrayList<LoopInfo> loopsInfo = new ArrayList<>();


    public MethodInfo(String className, MethodDeclaration methodDecl, int startPos, int len, String
            preAndPostCond) {
        this.className = className;
        this.methodDecl = methodDecl;
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
        if (matcher.find()) {
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
                    String preCondStr = matcher.group(2);
                    this.preCondList.add(ExpressionParser.parseExprStr(preCondStr));
                    break;

                case Patterns.ENSURES:
//                    System.out.println("@post : " + matcher.group(2));
                    String postCondStr = matcher.group(2);
                    this.postCondList.add(ExpressionParser.parseExprStr(postCondStr));
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
                    !nxtLoop.isPosInside(commentStartPos)) {
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

    public int getNumOfLoops() {
        return loopsInfo.size();
    }

    public ArrayList<Expression> getPreCondList() {
        ArrayList<Expression> copiedPreCondList = new ArrayList<>();
        copiedPreCondList.addAll(this.preCondList);
        return copiedPreCondList;
    }

    public ArrayList<Expression> getPostCondList() {
        ArrayList<Expression> copiedPostCondList = new ArrayList<>();
        copiedPostCondList.addAll(this.postCondList);
        return copiedPostCondList;
    }

    public String getClassName() {
        return className;
    }

    public String getRetVal() {
        return retVal;
    }

    public String getRetType() {
        return methodDecl.getReturnType2().toString();
    }

    public ArrayList<SingleVariableDeclaration> getFormalParams() {
        ArrayList<SingleVariableDeclaration> methodArgs = new ArrayList<>();
        methodArgs.addAll(methodDecl.parameters());
        return methodArgs;
    }

    public String getMethodName() {
        return methodDecl.getName().toString();
    }

    public String getQualifiedName() {
        return this.className + "." + methodDecl.getName().getFullyQualifiedName();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Transform the given string to K's ID.
     */
    public static String string2ID(String inputStr) {
        return "String2Id(\"" + inputStr + "\")";
    }

    /**
     * Transform the class name to K's ID.
     * @return
     */
    public static String className2ID(String clsName) {
        return "(class " + string2ID("." + clsName) + ")";
    }

    /**
     * Transform the method name to K's ID.
     * @return
     */
    public static String methodName2ID(String methName) {
        return string2ID(methName);
    }

    public String className2ID() {
        return className2ID(this.getClassName());
    }

    public String methodName2ID() {
        return methodName2ID(this.getMethodName());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        //method sig
        sb.append("Method " + getQualifiedName() + "'s args are \n");
        this.getFormalParams().forEach(param ->
                sb.append(param.getName().toString() + " of type " + param.getType().toString
                        () + "\n"));

        sb.append("Method " + this.getQualifiedName() + " 's contract is \n");

        this.preCondList.forEach(preCondStr -> {
            sb.append("@pre: " + preCondStr + ";\n");
        });
        this.postCondList.forEach(postCondStr -> {
            sb.append("@post: " + postCondStr + ";\n");
        });

        sb.append("@ret: " + this.retVal + ";\n");
        sb.append("\nLoop info is :\n");

        for (int index = 0; index < this.loopsInfo.size(); index++) {
            sb.append("Loop No. " + index + "'s info is :\n");
            sb.append(getLoopInfo(index));
        }

        return sb.toString();
    }
}
