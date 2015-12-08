package servlet;

import transform.Main;
import transform.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

@WebServlet(
        name = "MyServlet",
        urlPatterns = {"/HelloServlet"}
)
public class HelloServlet extends HttpServlet {

    private static final String TMP_FILE_NAME = "TMP_INPUT_JAVA_FILE.java";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        String inputPath = req.getParameter("path");
        String inputContent = req.getParameter("content");
        String str = "The path to the annotated file is: " + inputPath + "\n";
        str += "And the content of the java file is\n";
        out.write(str.getBytes());
        if (inputContent != null && !inputContent.trim().equals("")) {
            System.out.println(inputContent);
            Path inputJavaFilePath = FileUtils.getOutputFilePath(TMP_FILE_NAME);
            FileUtils.print2File(inputJavaFilePath, inputContent);
            inputPath = inputJavaFilePath.toString();

        } else if (inputPath != null) {
            String content = FileUtils.getContentFromURL(inputPath);
            out.write(content.getBytes());
        }

        Main.setAllowCache();//to ease the process of retrieving result.
        Main.main(new String[]{inputPath}); //parse the file.

        StringBuilder sb = new StringBuilder();
        sb.append("The corresponding k-spec is\n");
        sb.append(Main.getCachedResult() + "\n");

        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

}
