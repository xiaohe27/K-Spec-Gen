/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.28
 * Generated at: 2015-12-09 16:15:01 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <link href=\"google-code-prettify/prettify.css\" type=\"text/css\" rel=\"stylesheet\" />\n");
      out.write("    <script type=\"text/javascript\" src=\"google-code-prettify/prettify.js\"></script>\n");
      out.write("    <meta charset=\"UTF-8\">\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<script>\n");
      out.write("    function loadXMLDoc(myURL) {\n");
      out.write("        if (myURL == \"\")\n");
      out.write("            return;\n");
      out.write("        var xmlhttp;\n");
      out.write("        if (window.XMLHttpRequest) {\n");
      out.write("            xmlhttp = new XMLHttpRequest();\n");
      out.write("        } else {\n");
      out.write("            // code for older browsers\n");
      out.write("            xmlhttp = new ActiveXObject(\"Microsoft.XMLHTTP\");\n");
      out.write("        }\n");
      out.write("        xmlhttp.onreadystatechange = function() {\n");
      out.write("            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {\n");
      out.write("                document.getElementById(\"code\").value = xmlhttp.responseText;\n");
      out.write("                document.getElementById(\"javacode\").innerHTML =\n");
      out.write("                        xmlhttp.responseText;\n");
      out.write("                document.getElementById(\"javablock\").className = \"prettyprint\";\n");
      out.write("                prettyPrint();\n");
      out.write("            }\n");
      out.write("        };\n");
      out.write("        xmlhttp.open(\"GET\", myURL, true);\n");
      out.write("        xmlhttp.send();\n");
      out.write("    }\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("\n");
      out.write("<h2>Online K spec generator</h2>\n");
      out.write("\n");
      out.write("<form action=\"HelloServlet\" method=\"POST\" onsubmit=\"setContent()\">\n");
      out.write("    <select onchange=\"loadXMLDoc(this.value)\">\n");
      out.write("        <option value=\"\" selected=\"selected\"></option>\n");
      out.write("        <option\n");
      out.write("                value=\"https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/bst/bst.java\">\n");
      out.write("            bst.java\n");
      out.write("        </option>\n");
      out.write("        <option\n");
      out.write("                value=\"https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/list/listNode.java\">\n");
      out.write("            listNode.java\n");
      out.write("        </option>\n");
      out.write("\n");
      out.write("        <option value=\"https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/min/min.java\">min.java</option>\n");
      out.write("\n");
      out.write("        <option\n");
      out.write("                value=\"https://raw.githubusercontent.com/xiaohe27/K-Spec-Gen/master/KSpecGen/examples/sum/sum.java\">\n");
      out.write("            sum.java\n");
      out.write("        </option>\n");
      out.write("    </select>\n");
      out.write("\n");
      out.write("    <br/>\n");
      out.write("    <div><span><pre class=\"prettyprint\" id=\"javablock\">\n");
      out.write("<font size=\"5\">\n");
      out.write("    <code style=\"align-content: space-between\" class=\"language-java\" id=\"javacode\">\n");
      out.write("<textarea style=\"height: 15cm; width: 35cm\" wrap=\"hard\" id=\"pgmText\" ondblclick=\"this.value = ''\">\n");
      out.write("    Type your program here or choose an existing example from the left hand side popup menu.\n");
      out.write("</textarea>\n");
      out.write("    </code>\n");
      out.write("</font>\n");
      out.write("\n");
      out.write("</pre></span></div>\n");
      out.write("\n");
      out.write("    <textarea id=\"code\" name=\"content\" value=\"\" hidden></textarea>\n");
      out.write("\n");
      out.write("    <br/>\n");
      out.write("    <input type=\"submit\" value=\"!Submit!\"/>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("<script>\n");
      out.write("    function setContent() {\n");
      out.write("        if (document.getElementById(\"pgmText\") != null) {\n");
      out.write("            document.getElementById(\"code\").value =\n");
      out.write("                    document.getElementById(\"pgmText\").value;\n");
      out.write("        }\n");
      out.write("    }\n");
      out.write("</script>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
