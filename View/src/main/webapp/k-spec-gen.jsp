<!DOCTYPE html>
<%@page import="view.KSpecBean" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>K Spec for the input Java file</title>
</head>
<body>
<%KSpecBean kspec = (KSpecBean) request.getAttribute("bean");%>

<h6><%out.println(kspec.getAdditionalInfo());%></h6>

<h2>The content of the input java file is</h2>

<font size="5">
<code style="align-content: space-between"><%
    out.println(kspec.getInputJavaContent());
%></code>
</font>

<br />
  <h2>The k spec generated for the java file is</h2>
<font size="5">
<%--<code style="background-clip: border-box";>--%>
    <%
        out.println(kspec.getOutputKSpecContent());
    %>
<%--</code>--%>
</font>
</body>
</html>