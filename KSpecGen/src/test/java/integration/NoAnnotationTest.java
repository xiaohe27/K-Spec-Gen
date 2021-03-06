package integration;

import common.BasicTest;
import common.Const;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import transform.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

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
                {5}, {6}
        });
    }

    @Before
    public void setUp() {
        super.prepare();
    }

    @After
    public void tearDown() {
        super.clean();
    }

    @Test
    public void testOutput() throws IOException {
        String expectedStr = "";
        Main.main(new String[]{super.inputFile.getAbsolutePath()});
        assertEquals("There should be nothing generated from un-annotated java code",
                expectedStr, Main.getCachedResult());
    }
}
