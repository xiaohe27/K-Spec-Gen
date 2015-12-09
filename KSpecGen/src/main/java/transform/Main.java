package transform;

import parser.JavaParser;
import transform.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hx312 on 12/5/2015.
 */
public class Main {

    private static String cachedResult = null;

    private static boolean allowCache = false;

    //loop directory to get file list
    public static void ParseFilesInDir(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            String output = JavaParser.parse(file);

            if (allowCache)
                cachedResult = output;

            Path kspecPath = Paths.get(file.getAbsolutePath() + ".k");
            FileUtils.print2File(kspecPath, output);
            return;
        }

        String dirPath = file.getCanonicalPath() + File.separator;

        File root = new File(dirPath);

        System.out.println("File " + dirPath + " is a dir with contents:");
        System.out.println(root.listFiles());

        File[] files = root.listFiles();

        for (File f : files) {
            if (f.isFile()) {
                JavaParser.parse(f);
            }
        }
    }

    public static String getCachedResult() {
        return cachedResult;
    }

    public static void setAllowCache() {
        Main.allowCache = true;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Please provide exactly one argument which is the input path, can " +
                    "be either directory or .java file with annotation");
            System.exit(0);
        }

        //init the output dir
        init();

        String inputPath = args[0];
        if (inputPath.startsWith("https://raw.githubusercontent.com")
                && inputPath.endsWith(".java")) {
            String fileName = inputPath.substring(inputPath.lastIndexOf("/") + 1);
            //it is a source file in github
            System.err.println("Read the file " + fileName + " from github.");
            String content = FileUtils.getContentFromURL(inputPath);
//            System.out.println(content);
            Path inputJavaFilePath = FileUtils.getOutputFilePath(fileName);
            FileUtils.print2File(inputJavaFilePath, content);
            inputPath = inputJavaFilePath.toString();
//            System.out.println(inputPath + " is the path of the downloaded git file");
        }

        Main.ParseFilesInDir(inputPath);
    }

    private static void init() {
        File outputDir = new File(FileUtils.outputBasePath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        } else {
            if (outputDir.isFile()) {
                outputDir.delete();
                outputDir.mkdir();
            }
        }
    }
}
