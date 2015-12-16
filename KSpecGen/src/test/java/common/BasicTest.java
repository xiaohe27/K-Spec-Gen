package common;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Collection;

/**
 * Created by hx312 on 12/16/2015.
 */
public class BasicTest {
    protected File inputFile;
    protected File expectedFile;

    public BasicTest(int testId) {
        this.inputFile = new File(Const.inputFolder + testId + Const.extension);
        this.expectedFile = new File(Const.expectedFolder + testId + Const.extension);
    }
}
