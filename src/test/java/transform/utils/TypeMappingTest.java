package transform.utils;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import parser.ExpressionParser;
import parser.ast_visitor.ParamsVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by hx312 on 23/11/2015.
 */
@RunWith(Parameterized.class)
public class TypeMappingTest {
    ArrayList<SingleVariableDeclaration> formalParams;
    Expression jExpr;
    private String formalParamsStr;
    private String javaExpStr;
    private String expectedKExpStr;

    public TypeMappingTest(String formalParamsStr, String javaExpStr, String expectedKExpStr) {
        this.formalParamsStr = formalParamsStr;
        this.javaExpStr = javaExpStr;
        this.expectedKExpStr = expectedKExpStr;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"int a, int b", "a < b", "A <Int B"},
                {"int c, int d", "c == d", "C ==Int D"},
                {"int c, int d", "c != d", "C =/=Int D"},
                {"int alice, int bob", "alice < bob", "ALICE <Int BOB"},
                {"float c, float d, int e, int f", "e >= f + 7/2-3 && c/2.0 < d",
                        "E >=Int F +Int 7 /Int 2 -Int 3 andBool C /Float 2.0 <Float D"},
                {"boolean a, boolean b", "a || b", " A orBool B"},
                {"boolean b", "!b", "notBool B"},
                {"boolean a, boolean b", "a && !b", " A andBool notBool B"},
                {"boolean a, boolean b", "a != b", " A =/=Bool B"},
                {"String a, String b", "a != b", "AP =/=String BP"}
        });
    }

    @Before
    public void setUp() throws Exception {
        this.formalParams = new ArrayList<>();
        String code = MyTestHelper.getCodeWithMethodArgs(this.formalParamsStr);
        this.formalParams.addAll(ParamsVisitor.extractParamsFromStr(code));

        this.jExpr = ExpressionParser.parseExprStr(this.javaExpStr);
    }

    @After
    public void tearDown() throws Exception {
        this.formalParams.clear();
    }

    @Test
    public void testFromJExpr2KExpr() throws Exception {
        String actualKExprStr = CellContentGenerator.fromJExpr2KExprString(this.jExpr, this.formalParams);

        System.out.println("Actual output is " + actualKExprStr);
        assertEquals(this.expectedKExpStr.replaceAll("\\p{Blank}", "").trim(),
                actualKExprStr.replaceAll("\\p{Blank}", "").trim());
    }
}