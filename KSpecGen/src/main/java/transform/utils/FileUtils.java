package transform.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hx312 on 12/6/2015.
 */
public class FileUtils {

    private static String fileSeparator = System.getProperty("file.separator");
    public static String workingDir = System.getProperty("user.dir");
    public static String outputBasePath = workingDir + fileSeparator + "k-spec-output";

    public static void print2File(Path outputFile, String content) {
        byte[] rawContents = content.getBytes();
        if (outputFile != null) {
            try {
                outputFile.toFile().getParentFile().mkdirs();
                Files.write(outputFile, rawContents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] getClassPaths() {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = System.getProperty("java.class.path");
        String[] classPaths = classPath.split(pathSeparator);
        return classPaths;
    }

    /**
     * The implementation of this method is copied from oracle java's tutorial.
     * https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
     *
     * @param url The (github) url to be read.
     * @return The returned contents.
     */
    public static String getContentFromURL(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine + "\n");
        in.close();

        return sb.toString();
    }

    public static Path getOutputFilePath(String outputFileName) {
        return Paths.get(outputBasePath + fileSeparator + outputFileName);
    }

    public static String toAbsolutePath(String inputPath) {
        Path path = Paths.get(inputPath);

        return path.toAbsolutePath().toString();
    }
}
