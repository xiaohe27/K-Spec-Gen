package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by hx312 on 23/11/2015.
 */
public class TypeMappingTest {
    ArrayList<SingleVariableDeclaration> formalParams;

    @Before
    public void setUp() throws Exception {
        this.formalParams = new ArrayList<>();

//        expect(varA.getType()).andReturn(type);
//
//        this.formalParams.add(varA);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFromJExpr2KExpr() throws Exception {
//        System.out.println("varA is " + this.formalParams.get(0).getType());
    }
}