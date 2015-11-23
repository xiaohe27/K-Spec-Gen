package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import mockit.*;

/**
 * Created by hx312 on 23/11/2015.
 */
public class TypeMappingTest {
    ArrayList<SingleVariableDeclaration> formalParams;


    @Before
    public void setUp() throws Exception {
        this.formalParams = new ArrayList<>();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFromJExpr2KExpr(@Mocked final SingleVariableDeclaration varA) throws Exception {

        new Expectations(){{
            varA.toString(); result = "int";
        }};
    }
}