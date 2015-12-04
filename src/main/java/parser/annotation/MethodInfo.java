package parser.annotation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import parser.ExpressionParser;
import transform.utils.Utils;

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
    private ArrayList<String> postCondList = new ArrayList<>();
    //the expected return value according to the method contract.
    private String expectedRetVal;
    //the return expression
    private Expression retExpr;

    /**
     * The index is the pos of the loop:
     * If in the ordinary traversal, the loop is the nth to be visited,
     * then its index is n.
     */
    private ArrayList<LoopInfo> loopsInfo = new ArrayList<>();
    /**
     * The user provided objectStore content.
     */
    private String objStoreContent;


    public MethodInfo(String className, MethodDeclaration methodDecl, int startPos, int len, String
            preAndPostCond) {
        this.className = className;
        this.methodDecl = methodDecl;
        this.startPos = startPos;
        this.endPos = startPos + len;

        parseMethodContract(preAndPostCond);
    }

    public void setRetExpr(final Expression retExpr0) {
        this.retExpr = retExpr0;
    }

    public boolean isInsideMethod(int pos) {
        return pos >= this.startPos && pos < this.endPos;
    }

    private void parseMethodContract(String javaDocStr) {
        Matcher matcher = Patterns.METHOD_CONTRACT.matcher(javaDocStr);
        int count = 0;
        int groupSize = matcher.groupCount();
//        System.out.println("Group size is " + groupSize);

        String contractStr = null;
        if (matcher.find()) {
            contractStr = matcher.group(1);
        }

        //if the javadoc is not a method contract, then ignore it.
        if (contractStr == null) {
            System.out.println("No match for the method annotation is found.");
            return;
        }

        matcher = contractStr.matches(Patterns.SingleClause.pattern()) ?
                Patterns.SingleClause.matcher(contractStr) :
                Patterns.ObjStoreCellPattern.matcher(contractStr);

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
                    this.postCondList.add(postCondStr);
                    break;

                case Patterns.RETURNS:
//                    System.out.println("@ret : " + matcher.group(2));
                    this.expectedRetVal = matcher.group(2);
                    break;

                case Patterns.OBJStore:
//                    System.out.println("@objStore : " + matcher.group(2));
                    this.objStoreContent = matcher.group(2);
                    break;
                default:
                    break;
            }
        }
    }

    public void addPotentialLI(String loopInv, int commentStartPos) {
        this.loopsInfo.stream()
                .filter(loopInfo -> loopInfo.isPosInside(commentStartPos))
                .min((loopInfo1, loopInfo2) -> (loopInfo1.srcCodeSize() - loopInfo2.srcCodeSize()))
                .ifPresent(tarLoopInfo -> {
                    tarLoopInfo.addLI(loopInv);
                });
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
        return this.preCondList;
    }

    public ArrayList<String> getPostCondList() {
        return this.postCondList;
    }

    public String getClassName() {
        return className;
    }

    public String getExpectedRetVal() {
        if (expectedRetVal == null) {
            Character firstCharInType = this.getRetType().toString().charAt(0);
            return "?" + firstCharInType.toString().toUpperCase();
        }

        return expectedRetVal;
    }

    public Type getRetType() {
        return methodDecl.getReturnType2();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public String className2ID() {
        return Utils.className2ID(this.getClassName());
    }

    public String methodName2ID() {
        return Utils.methodName2ID(this.getMethodName());
    }

    public String getKModuleName() {
        String methodName = this.getMethodName();
        String modName = this.className + "-" + (methodName.equals(this.className) ? ""
                : methodName + "-") + "SPEC";

        return modName.toUpperCase();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        //method sig
        sb.append("Method " + getQualifiedName() + "'s args are \n");
        this.getFormalParams().forEach(param ->
                sb.append(param.getName().toString() + " of type " + param.getType().toString
                        () + "\n"));

        if (this.retExpr != null) {
            sb.append("The method returns `" + this.retExpr + "`.\n");
        }

        sb.append("Method " + this.getQualifiedName() + " 's contract is \n");

        this.preCondList.forEach(preCondStr -> {
            sb.append("@pre: " + preCondStr + ";\n");
        });
        this.postCondList.forEach(postCondStr -> {
            sb.append("@post: " + postCondStr + ";\n");
        });

        if (this.objStoreContent != null) {
            sb.append("@objectStore: {" + this.objStoreContent + "}\n");
        }

        sb.append("@ret: " + this.expectedRetVal + ";\n");
        sb.append("\nLoop info is :\n");

        for (int index = 0; index < this.loopsInfo.size(); index++) {
            sb.append("Loop No. " + index + "'s info is :\n");
            sb.append(getLoopInfo(index));
        }

        return sb.toString();
    }

    public void addEnvMap(String envCellInfo, int commentStartPos) {
        this.loopsInfo.stream()
                .filter(loopInfo -> loopInfo.isPosInside(commentStartPos))
                .min((loopInfo1, loopInfo2) -> (loopInfo1.srcCodeSize() - loopInfo2.srcCodeSize()))
                .ifPresent(tarLoopInfo -> {
                    tarLoopInfo.addEnvInfo(envCellInfo);
                });
    }

    public void addStoreMap(String storeCellInfo, int commentStartPos) {
        this.loopsInfo.stream()
                .filter(loopInfo -> loopInfo.isPosInside(commentStartPos))
                .min((loopInfo1, loopInfo2) -> (loopInfo1.srcCodeSize() - loopInfo2.srcCodeSize()))
                .ifPresent(tarLoopInfo -> {
                    tarLoopInfo.addStoreInfo(storeCellInfo);
                });
    }
}
