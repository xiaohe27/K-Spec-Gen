package transform.utils;

import common.BasicTest;
import common.Const;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import parser.ast_visitor.LoopVisitor;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class KAST_TransformerTest extends BasicTest {
    //The while stmt node used in the test
    private WhileStatement whileNd;

    public KAST_TransformerTest(int testId) {
        super(testId);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1}, {2}, {3}, {4}
        });
    }

    @Before
    public void setUp() throws Exception {
        this.whileNd = MyTestHelper.extractWhileNdFromJavaFile(inputFile);
//        System.out.println("Expected file " + this.expectedFile.getName() + " exists ? " +
//                expectedFile.exists());
    }

    @After
    public void tearDown() throws Exception {
        this.whileNd = null;
    }

    @Test
    public void testConvert2KAST() throws Exception {
        String expected = new String(Files.readAllBytes(this.expectedFile.toPath()));
        LoopVisitor loopVisitor = new LoopVisitor();
        this.whileNd.accept(loopVisitor);

        assertEquals(expected.trim().replaceAll("\\p{Space}", ""),
                loopVisitor.getLoopASTString().trim().replaceAll("\\p{Space}", ""));
    }
}