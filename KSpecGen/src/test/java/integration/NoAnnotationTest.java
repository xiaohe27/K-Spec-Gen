package integration;

import common.BasicTest;
import common.Const;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hx312 on 12/15/2015.
 */
@RunWith(Parameterized.class)
public class NoAnnotationTest extends BasicTest {
    public NoAnnotationTest(int testId) {
        super(testId, Const.inExt, Const.outExt);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
//                {5}, {6}
        });
    }

    public void testOutput() {

    }
}
