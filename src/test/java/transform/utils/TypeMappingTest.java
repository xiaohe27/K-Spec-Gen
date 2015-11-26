package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;

/**
 * Created by hx312 on 23/11/2015.
 */
@RunWith(Parameterized.class)
public class TypeMappingTest {
    ArrayList<SingleVariableDeclaration> formalParams;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"int a, int b", "a < b", "A <Int B"}
        });
    }

    private String formalParamsStr;
    private String javaExpStr;
    private String kExpStr;

    public TypeMappingTest(String formalParamsStr, String javaExpStr, String kExpStr) {
        this.formalParamsStr = formalParamsStr;
        this.javaExpStr = javaExpStr;
        this.kExpStr = kExpStr;
    }

    @Before
    public void setUp() throws Exception {
        this.formalParams = new ArrayList<>();

    }

    @After
    public void tearDown() throws Exception {
        this.formalParams.clear();
    }

    @Test
    public void testFromJExpr2KExpr() throws Exception {
    }
}