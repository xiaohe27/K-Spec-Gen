package common;

import java.io.File;

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

    public BasicTest(int testId, String inputSuffix, String outputSuffix) {
        this.inputFile = new File(Const.inputFolder + testId + inputSuffix);
        this.expectedFile = new File(Const.expectedFolder + testId + outputSuffix);
    }
}
