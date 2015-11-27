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
                {"int a, int b", "a < b", "A <Int B"}
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
        String actualKExprStr = TypeMapping.fromJExpr2KExprString(this.jExpr, this.formalParams);

        System.out.println("Actual output is " + actualKExprStr);
        assertEquals(this.expectedKExpStr.trim(), actualKExprStr.trim());
    }
}