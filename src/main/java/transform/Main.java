package transform;

import parser.JavaParser;
import transform.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hx312 on 12/5/2015.
 */
public class Main {

    //loop directory to get file list
    public static void ParseFilesInDir(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            String output = JavaParser.parse(file);
            Path kspecPath = Paths.get(file.getAbsolutePath() + ".k");
            Utils.print2File(kspecPath, output);
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

    public static String[] getClassPaths() {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = System.getProperty("java.class.path");
        String[] classPaths = classPath.split(pathSeparator);
        return classPaths;
    }

    public static void main(String[] args) throws IOException {
        Main.ParseFilesInDir(args[0]);
    }
}
