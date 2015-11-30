package transform.utils;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.ExpressionParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hx312 on 19/11/2015.
 */
public class TypeMapping {
    /**
     * The type of the operands, 0 for int and 1 for floating point numbers.
     */
    public static final int INT_OPERAND = 0;
    public static final int FLOAT_OPERAND = 1;
    public static final int STRING_OPERAND = 2;
    public static final int BOOL_OPERAND = 3;
    public static final int OTHER_OPERAND = 4;

    private final static String[] intTypes = {"byte", "short", "char", "int", "long"};
    private final static String[] floatTypes = {"float", "double"};
    private final static String[] prefixOp = {"!", "-", "+"};

    private final static String[] commonInfixOP = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=",
            "==", "!="};

    private final static String[] intSpecOP = {"<<", ">>", "~", "^", "&", "|"};
    private final static String[] boolOP = {"&&", "||", "!=", "==", "!"};

    private final static String[] infixOP = initInfixOPs();

    private static String[] initInfixOPs() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < commonInfixOP.length; i++) {
            list.add(commonInfixOP[i]);
        }
        for (int i = 0; i < intSpecOP.length; i++) {
            list.add(intSpecOP[i]);
        }

        String[] tmpStrArr = new String[commonInfixOP.length + intSpecOP.length];
        return list.toArray(tmpStrArr);
    }


    private static boolean isIn(String[] strArr, String tarStr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(tarStr))
                return true;
        }
        return false;
    }

    public static int getTypeId(String type) {
        if (type == null)
            return OTHER_OPERAND;

        if (isIn(intTypes, type)) {
            return INT_OPERAND;
        } else if (isIn(floatTypes, type)) {
            return FLOAT_OPERAND;
        } else if ("boolean".equals(type)) {
            return BOOL_OPERAND;
        } else if ("String".equals(type)) {
            return STRING_OPERAND;
        } else {
            return OTHER_OPERAND;
        }
    }

    public static String getKBuiltInType4SimpleJType(String javaType) {
        if (isIn(intTypes, javaType))
            return "Int";

        if (isIn(floatTypes, javaType))
            return "Float";

        if ("String".equals(javaType))
            return "String";

        if ("boolean".equals(javaType))
            return "Bool";

        //it is not a basic type, then it should be a ref type.
        return "RawRefVal";
    }

    /**
     * Generate the k var name based on 'baseName'.
     * The general strategy is
     * 1) capitalize the baseName to generate word w.
     * 2) if it is a primitive type, then goto 3), otherwise goto 4).
     * 3) If the string w produced in step 1) is different from the baseName, then return w; if
     * they are the same, then return w + "_raw".
     * 4) return w + "P".
     *
     * @param baseName
     * @param isPrimitive
     * @return
     */
    public static String convert2KVar(String baseName, boolean isPrimitive) {
        if (baseName == null)
            return "NULL";

        //step 1
        String freshVar = baseName.toUpperCase();

        //step 2
        if (isPrimitive) {
            //step 3
            if (freshVar.equals(baseName)) {
                return freshVar + "_raw";
            } else {
                return freshVar;
            }
        } else {
            //step 4
            return freshVar + "P";
        }
    }

    public static String convert2KType(SingleVariableDeclaration v) {
        String jType = v.getType().toString();
        String varName = v.getName().toString();
        boolean isPrimitive = v.getType().isPrimitiveType();

        String jTypeInJavaSemantics = isPrimitive ? jType : Utils.className2ID(jType);

        String result = convert2KVar(varName, isPrimitive);
        result += ":" + getKBuiltInType4SimpleJType(jType);
        result = isPrimitive ? result : "(" + result + ")";
        result += "::" + jTypeInJavaSemantics;
        return result;
    }

    public static String convert2KOP(String oldOp, int operandType) {
        oldOp = oldOp.trim();
        if ("!".equals(oldOp)) {
            return "notBool";
        } else if ("&&".equals(oldOp)) {
            return "andBool";
        } else if ("||".equals(oldOp)) {
            return "orBool";
        }

        switch (operandType) {
            case INT_OPERAND:
                return covert2KOp_Int(oldOp);

            case FLOAT_OPERAND:
                return covert2KOp_Float(oldOp);

            case STRING_OPERAND:
                return covert2KOp_String(oldOp);

            case BOOL_OPERAND:
                return convert2KOp_Bool(oldOp);

            default:
                return oldOp;

        }
    }

    private static String convert2KOp_Bool(String oldOp) {
        if ("==".equals(oldOp)) {
            return "==Bool";
        } else if ("!=".equals(oldOp)) {
            return "=/=Bool";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_Int(String oldOp) {
        if ("!=".equals(oldOp)) {
            return "=/=Int";
        } else if (isIn(infixOP, oldOp)) {
            return oldOp + "Int";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_Float(String oldOp) {
        if ("!=".equals(oldOp)) {
            return "=/=Float";
        } else if (isIn(commonInfixOP, oldOp)) {
            return oldOp + "Float";
        } else {
            return oldOp;
        }
    }

    public static String covert2KOp_String(String oldOp) {
        String[] excludeList = {"-", "*", "/", "%"};
        if ("!=".equals(oldOp)) {
            return "=/=String";
        } else if (isIn(commonInfixOP, oldOp) && !isIn(excludeList, oldOp)) {
            return oldOp + "String";
        } else {
            return oldOp;
        }
    }


    public static HashMap<String, String> extractJVar2KVarMapping(ArrayList<SingleVariableDeclaration> formalParams) {
        //map java var name to k var name
        HashMap<String, String> fromJVarName2KVarName = new HashMap<>();

        formalParams.forEach(varDecl -> fromJVarName2KVarName.put(varDecl.getName().toString(),
                TypeMapping.convert2KVar(varDecl.getName().toString(), varDecl.getType().isPrimitiveType())));
        return fromJVarName2KVarName;
    }

    /**
     * Construct the KExpr from JavaExpression; Decompose the jexpr into conjunctions of
     * disjunctions, and transform each disjunction separately. Then the final result can be a
     * combination of sub-results via 'andBool' K-operator.
     *
     * @param jexpr        The input java expression that represents the pre/post condition of the
     *                     annotated method.
     * @param formalParams The formal parameters of the annotated method.
     * @return The corresponding k expression for the input java expression.
     */
    public static String fromJExpr2KExprString(Expression jexpr, ArrayList<SingleVariableDeclaration>
            formalParams) {
        //map k-var to java-type
        HashMap<String, String> typeEnv = new HashMap<>();
        HashMap<String, String> fromJVarName2KVarName = extractJVar2KVarMapping(formalParams);

        formalParams.forEach(varDecl -> typeEnv.put(fromJVarName2KVarName.get
                (varDecl.getName().toString()), varDecl.getType().toString()));

        return fromJExpr2KExprString(jexpr, fromJVarName2KVarName, typeEnv);
    }

    private static String fromJExpr2KExprString(Expression jexpr, HashMap<String, String>
            fromJVarName2KVarName, HashMap<String, String> typeEnv) {
        String jexprStr = ExpressionParser.printExprWithKVars(jexpr, fromJVarName2KVarName);

        String[] disjuncts = jexprStr.split("&&");


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < disjuncts.length - 1; i++) {
            String disjunction = disjuncts[i];
            String subResult = fromDisjunct2KExpr(disjunction, typeEnv);
            sb.append(subResult + " andBool ");
        }

        sb.append(fromDisjunct2KExpr(disjuncts[disjuncts.length - 1], typeEnv) + "\n");

        return sb.toString();
    }

    private static String fromDisjunct2KExpr(String disjunction, HashMap<String, String> typeEnv) {
        String[] literals = disjunction.split("\\|\\|");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.length; i++) {
            String literal = literals[i];
            int typeId = ExpressionParser.getTypeIdOfTheExpr(literal, typeEnv);

            String subResult = fromLiteral2KExpr(literal, typeId);
            sb.append(subResult + (i == literals.length - 1 ? " " : " orBool "));
        }

        return sb.toString();
    }


    private static String toRegEx(String exp) {
        if (exp == null) return "";

        if (exp.equals("+") || exp.equals("*"))
            return "\\" + exp;

        if (exp.equals("<") || exp.equals(">"))
            return exp + "(?!=)";

        return exp;
    }

    /**
     * We can assume that there is neither '&&' nor '||' in the literal, and focus on
     * transforming the literal according to the type of atoms.
     * Currently, only int operations and floating operations are supported.
     *
     * @param literal
     * @param typeId
     * @return
     */
    public static String fromLiteral2KExpr(String literal, int typeId) {
        String kexp = literal;
        //replace each pattern in the array to its corresponding k-form.
        String[] transformCandidates = null;
        if (typeId == TypeMapping.INT_OPERAND)
            transformCandidates = TypeMapping.infixOP;
        else if (typeId == TypeMapping.FLOAT_OPERAND)
            transformCandidates = TypeMapping.commonInfixOP;
        else if (typeId == TypeMapping.BOOL_OPERAND)
            transformCandidates = TypeMapping.boolOP;
        else if (typeId == TypeMapping.STRING_OPERAND)
            transformCandidates = TypeMapping.commonInfixOP;
        else transformCandidates = null;

        if (transformCandidates == null)
            return literal;

        for (int i = 0; i < transformCandidates.length; i++) {
            String curOp = transformCandidates[i];
            if (!literal.contains(curOp))
                continue;

            kexp = kexp.replaceAll(toRegEx(curOp), TypeMapping.convert2KOP(curOp, typeId));
        }

        return kexp;
    }


    public static void main(String[] args) {
        String goodExp = ExpressionParser.parseExprStr("min(a,b) + c -d/e >= 72").toString();
        System.out.println(fromLiteral2KExpr(goodExp, TypeMapping.INT_OPERAND));
    }

}
