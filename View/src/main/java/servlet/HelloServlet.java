package servlet;

import transform.Main;
import transform.utils.FileUtils;
import view.KSpecBean;

import javax.servlet.RequestDispatcher;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        ServletOutputStream out = response.getOutputStream();

        KSpecBean bean = new KSpecBean();
        request.setAttribute("bean",bean);

        String inputPath = request.getParameter("path");
        String inputContent = request.getParameter("content");
        String additionalInfo = "The path to the annotated file is: " + inputPath + "\n";
        bean.setAdditionalInfo(additionalInfo);
        if (inputContent != null && !inputContent.trim().equals("")) {
//            System.out.println(inputContent);
            Path inputJavaFilePath = FileUtils.getOutputFilePath(TMP_FILE_NAME);
            FileUtils.print2File(inputJavaFilePath, inputContent);
            inputPath = inputJavaFilePath.toString();
            bean.setInputJavaContent(inputContent);
        } else if (inputPath != null) {
            String content = FileUtils.getContentFromURL(inputPath);
            bean.setInputJavaContent(content);
        }

        Main.setAllowCache();//to ease the process of retrieving result.
        Main.main(new String[]{inputPath}); //parse the file.

        StringBuilder sb = new StringBuilder();
        sb.append(Main.getCachedResult() + "\n");

        bean.setOutputKSpecContent(sb.toString());

        RequestDispatcher rd=request.getRequestDispatcher("k-spec-gen.jsp");
        rd.forward(request, response);
//        out.flush();
//        out.close();
    }

}
