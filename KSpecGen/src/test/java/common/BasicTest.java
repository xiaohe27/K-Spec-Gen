package common;

import transform.Main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    protected void prepare() {
        Main.setAllowCache();
    }

    protected void clean() {
        Path kspecPath = Paths.get(this.inputFile.getAbsolutePath() + ".k");
        if (kspecPath != null && kspecPath.toFile().exists()) {
            kspecPath.toFile().delete();
        }
    }
}
