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
    private static String outputPath = System.getProperty("user.dir")
            + System.getProperty("file.separator") + "k-spec-output";

    //loop directory to get file list
    public static void ParseFilesInDir(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            String output = JavaParser.parse(file);
            Path kspecPath = Paths.get(file.getAbsolutePath() + ".k");
            FileUtils.print2File(kspecPath, output);
            return;
        }

        String dirPath = file.getCanonicalPath() + File.separator;

        File root = new File(dirPath);
        //System.out.println(rootDir.listFiles());
        File[] files = root.listFiles();
        String filePath = null;

        for (File f : files) {
            filePath = f.getAbsolutePath();
            if (f.isFile()) {
                JavaParser.parse(f);
            }
        }
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
            System.out.println("Read the file " + fileName + " from github.");
            String content = FileUtils.getContentFromURL(inputPath);
//            System.out.println(content);

        } else {
            Main.ParseFilesInDir(inputPath);
        }

    }

    private static void init() {
        File outputDir = new File(outputPath);
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
