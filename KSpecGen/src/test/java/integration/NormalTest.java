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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by xiaohe on 12/16/15.
 */
@RunWith(Parameterized.class)
public class NormalTest extends BasicTest {
    public NormalTest(int testId) {
        super(testId, Const.inExt, Const.outExt);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {7}, {8}, {9}, {10}
        });
    }

    @Before
    public void setUp() {
        Main.setAllowCache();
    }

    @After
    public void tearDown() {
        Path kspecPath = Paths.get(super.inputFile.getAbsolutePath() + ".k");
        if (kspecPath != null && kspecPath.toFile().exists()) {
            kspecPath.toFile().delete();
        }
    }

    @Test
    public void testOutput() throws IOException {
        String expectedStr = new String(Files.readAllBytes(super.expectedFile.toPath()))
                .trim().replaceAll("\\p{Space}", "");
        Main.main(new String[]{super.inputFile.getAbsolutePath()});

        assertEquals("The generated k-spec is not the same as expected.",
                expectedStr,
                Main.getCachedResult().trim().replaceAll("\\p{Space}", ""));
    }
}
