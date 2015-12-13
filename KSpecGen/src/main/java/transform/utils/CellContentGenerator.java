package transform.utils;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import parser.ExpressionParser;
import transform.ast.rewrite.KRewriteObj;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by hx312 on 12/5/2015.
 */
public class CellContentGenerator {
    public static void updateObjStoreByParsingContent(List<String> objStore, String objStoreContent, Collection<KRewriteObj> kObjs) {
        if (objStoreContent == null || objStore == null)
            return;

        final String[] inputs = objStoreContent.split(",");
        for (int i = 0; i < inputs.length; i++) {
            String strI = inputs[i];
            final String[] elements = strI.split("=>");
            final String[] lhs = {elements[0]};
            final String[] rhs = {null};
            if (elements.length > 1)
                rhs[0] = elements[1];

            kObjs.forEach(kRewriteObj -> {
                lhs[0] = kRewriteObj.rewrite2KVarIfPossible(lhs[0], true);
            });

            if (rhs[0] != null) {
                kObjs.forEach(kRewriteObj -> {
                    rhs[0] = kRewriteObj.rewrite2KVarIfPossible(rhs[0], false);
                });
            }

            String outputI = lhs[0];
            if (rhs[0] != null) {
                outputI += " => " + rhs[0];
            }

            objStore.add(outputI);
        }
    }

    private static HashMap<String, String> extractJVar2KVarMapping
            (ArrayList<SingleVariableDeclaration> formalParams) {
        //map java var name to k var name
        HashMap<String, String> fromJVarName2KVarName = new HashMap<>();

        formalParams.forEach(varDecl -> fromJVarName2KVarName.put(varDecl.getName().toString(),
                TypeMapping.convert2KVar(varDecl.getName().toString(), varDecl.getType().isPrimitiveType())));
        return fromJVarName2KVarName;
    }

    private static HashMap<String, String> extractJVar2KVarMapping
            (Stream<SimpleName> names) {
        //map java var name to k var name
        HashMap<String, String> fromJVarName2KVarName = new HashMap<>();

        names.forEach(name -> {
            String varId = name.getIdentifier();
            fromJVarName2KVarName.put(varId,
                    TypeMapping.convert2KVar(varId, name.resolveTypeBinding().isPrimitive()));
        });
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

    public static String fromJExpr2KExprString(Expression jexpr, Set<SimpleName> names) {
        HashMap<String, String> typeEnv = new HashMap<>();
        HashMap<String, String> fromJVarName2KVarName = extractJVar2KVarMapping(names.stream());

        names.forEach(name -> {
            typeEnv.put(fromJVarName2KVarName.get(name.getIdentifier()),
                    name.resolveTypeBinding().getName());
        });

        return fromJExpr2KExprString(jexpr, fromJVarName2KVarName, typeEnv);
    }

    private static String fromJExpr2KExprString(Expression jexpr, HashMap<String, String>
            fromJVarName2KVarName, HashMap<String, String> typeEnv) {
        String jexprStr = ExpressionParser.printExprWithKVars(jexpr, fromJVarName2KVarName);

        return fromJExpr2KExprString(jexprStr, typeEnv);
    }

    public static String fromJExpr2KExprString(String jexprStr,
                                               ArrayList<SingleVariableDeclaration> params) {
        HashMap<String, String> jvar2KvarMapping = extractJVar2KVarMapping(params);
        HashMap<String, String> typeEnv = new HashMap<>();

        params.forEach(var -> {
            typeEnv.put(jvar2KvarMapping.get(var.getName().getIdentifier()),
                    var.getType().toString());
        });

        final String[] output = new String[]{jexprStr};
        jvar2KvarMapping.forEach((oldName, newName) -> {
            output[0] = output[0].replaceAll("(?<=\\W)" + oldName + "(?=\\W)", newName);
        });

        Pattern freshVarPattern = Pattern.compile("(?<=\\?)(\\w+)");
        Matcher matcher = freshVarPattern.matcher(output[0]);
        while (matcher.find()) {
            String varName = matcher.group(1);
            output[0] = output[0].replaceAll(varName, varName.toUpperCase());
        }

        return fromJExpr2KExprString(output[0], typeEnv);
    }

    private static String fromJExpr2KExprString(String jexprStr, HashMap<String, String> typeEnv) {
        String[] disjuncts = jexprStr.split("&&");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < disjuncts.length - 1; i++) {
            String disjunction = disjuncts[i];
            String subResult = fromDisjunct2KExpr(disjunction, typeEnv);
            sb.append(subResult + " andBool ");
        }

        sb.append(fromDisjunct2KExpr(disjuncts[disjuncts.length - 1], typeEnv));

        return sb.toString();
    }

    private static String fromDisjunct2KExpr(String disjunction, HashMap<String, String> typeEnv) {
        String[] literals = disjunction.split("\\|\\|");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.length; i++) {
            String literal = literals[i];
            int typeId = ExpressionParser.getTypeIdOfTheExpr(literal, typeEnv);

            String subResult = fromLiteral2KExpr(literal, typeId);
            //dirty hack that make the == of type K if there exists special op like membership test
            if (literal.contains("in")) {
                subResult = subResult.replaceAll("==Int", " ==K ");
                subResult = subResult.replaceAll("==Float", " ==K ");
                subResult = subResult.replaceAll("==(?!K)", "==K");
            }
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

        if (exp.equals("in"))
            return "(?<=\\p{Space})in(?=\\p{Space})";
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
}
