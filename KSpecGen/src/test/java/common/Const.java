package common;

/**
 * Created by hx312 on 12/15/2015.
 */
public class Const {
    private static final String fileSep = System.getProperty("file.separator");
    private static final String basePath = System.getProperty("user.dir") + fileSep +
            "src" + fileSep + "test" + fileSep + "resources" + fileSep;
    public static final String expectedFolder = basePath + "expected" + fileSep;
    public static final String inputFolder = basePath + "in" + fileSep;
    public static final String extension = ".txt";
    public static final String inExt = ".java.in";
    public static final String outExt = ".out";
}
