package transform.utils;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * Created by hx312 on 23/11/2015.
 */
public class TypeMappingTest {
    ArrayList<SingleVariableDeclaration> formalParams;

    @Before
    public void setUp() throws Exception {
        this.formalParams = new ArrayList<>();
        SingleVariableDeclaration varA = mock(SingleVariableDeclaration.class);
        expect(varA.getType()).andReturn(new Type() {
        });

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFromJExpr2KExpr() throws Exception {

    }
}