package transform.utils;

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
public class KAST_TransformerTest {
    private int testId;
    private static final String fileSep = System.getProperty("file.separator");
    private static final String basePath = System.getProperty("user.dir") + fileSep +
            "src" + fileSep + "test" + fileSep + "resources" + fileSep;

    private static final String inputFolder = basePath + "in" + fileSep;
    private static final String expectedFolder = basePath + "expected" + fileSep;
    private static final String extension = ".txt";

    //The while stmt node used in the test
    private WhileStatement whileNd;

    private File expectedFile;

    public KAST_TransformerTest(int testId) {
        this.testId = testId;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1}, {2}, {3}
        });
    }

    @Before
    public void setUp() throws Exception {
        File inputFile = new File(inputFolder + this.testId + extension);
        this.whileNd = MyTestHelper.extractWhileNdFromJavaFile(inputFile);

        this.expectedFile = new File(expectedFolder + this.testId + extension);
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
//        System.out.println(expected);
        LoopVisitor loopVisitor = new LoopVisitor();
        this.whileNd.accept(loopVisitor);
        assertEquals(expected.trim().replaceAll("\\p{Space}", ""),
                loopVisitor.getLoopASTString().trim().replaceAll("\\p{Space}", ""));
    }
}